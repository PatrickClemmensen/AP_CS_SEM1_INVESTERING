package service;

import model.asset.Stock;
import model.portfolio.Portfolio;
import model.portfolio.Position;
import model.portfolio.User;
import model.transaction.Transaction;
import model.transaction.OrderType;
import util.AppConstants;
import util.constants.Colors;
import util.csv.CSVReader;
import util.exception.InsufficientFundsException;
import util.exception.InsufficientQuantityException;
import util.validation.CashBalanceValidator;
import util.validation.TickerValidator;
import util.validation.QuantityValidator;

import javax.swing.plaf.basic.BasicDesktopIconUI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static util.AppConstants.TRANSACTIONS_FILE;

/**
 * Service class responsible for managing user portfolios and stock transactions.
 * <p>
 *     Handles buying and selling of stocks, loading portfolios from persistent
 *     storage, and retrieving transaction history. All operations are validated
 *     before execution using the appropriate validator classes.
 * </p>
 */
public class PortfolioService {
    private StockMarketService marketService;

    /**
     * Constructs a new {@code PortfolioService} backed by the given market service.
     *
     * @param marketService the service used to find stock by ticker
     */
    public PortfolioService(StockMarketService marketService) {
        this.marketService = marketService;
    }

    /**
     * The method finds the stock by ticker, validates that the stock exists and that the quantity is greater than zero.
     * It then calculates the total cost of the purchase and checks whether the user has enough available cash.
     * <p>
     * If the purchase is valid, the total cost is deducted from the user's cash balance.
     * The stock is then added to the user's portfolio.
     * If the user already owns the stock, the existing position is updated with the additional quantity and purchase price.
     * </p>
     * @param user the user buying the stock
     * @param ticker the ticker of the stock to buy
     * @param quantity the amount of stocks to buy
     * @return a {@link Transaction} record of the purchase
     * @throws util.exception.InvalidAssetException         if the ticker does not exist in the market
     * @throws IllegalArgumentException                     if the quantity is zero or negative
     * @throws util.exception.InsufficientFundsException    if the user cannot afford the purchase
     */
    public Transaction buy(User user, String ticker, int quantity) {
        TickerValidator.validate(ticker, marketService);
        Stock stock = marketService.findByTicker(ticker);

        QuantityValidator.validate(quantity);

        double totalCost = stock.getPrice()*quantity;

        CashBalanceValidator.validate(user, totalCost);

        user.deductCash(totalCost);

        Position existing = user.getPortfolio().findByTicker(ticker);
        if (existing != null){
            existing.increaseQuantity(quantity, stock.getPrice());
        } else {
            user.getPortfolio().addPosition(new Position(stock,quantity,stock.getPrice()));
        }

        return new Transaction(
                user.getUserId(),
                LocalDate.now(),
                ticker,
                stock.getPrice(),
                stock.getCurrency(),
                OrderType.BUY,
                quantity
        );
    }

    /**
     * Executes a stock for the given user.
     * <p>
     *     Validates the ticker and quantity before proceeding. The user must own
     *     sufficient shares of the stock to complete the sale. The proceeds are
     *     credited to the user's cash balance, and the position is reduced accordingly.
     *     If all shares are sold, the position is removed from the portfolio entirely.
     * </p>
     * @param user      the user selling the stock
     * @param ticker    the ticker of the stock to sell
     * @param quantity  the number of stocks to sell
     * @return a {@link Transaction} record of the sale
     * @throws util.exception.InvalidAssetException         if the ticker does not exist in the market
     * @throws IllegalArgumentException                     if the quantity is zero or negative
     * @throws util.exception.InsufficientQuantityException if the user does not own enough shares
     */
        public Transaction sell(User user, String ticker, int quantity) {
            TickerValidator.validate(ticker, marketService);
            QuantityValidator.validate(quantity);

            Stock stock = marketService.findByTicker(ticker);
            Portfolio portfolio = user.getPortfolio();
            Position position = portfolio.findByTicker(ticker);

            if (position == null || position.getQuantity() < quantity) {
                throw new InsufficientQuantityException("Insufficient quantity for ticker " + ticker);
            }

            double totalValue = stock.getPrice() * quantity;
            user.addCash(totalValue);
            position.decreaseQuantity(quantity);

            if (position.getQuantity() == 0) {
                portfolio.removePosition(position);
            }

            return new Transaction(
                    user.getUserId(),
                    LocalDate.now(),
                    ticker,
                    stock.getPrice(),
                    AppConstants.BASE_CURRENCY,
                    OrderType.SELL,
                    quantity
            );
        }

    /**
     * Reads Transaction.csv, updates a given users portfolio with positions.
     * @param user user which portfolio is loaded
     */
    public void loadPortfolio(User user) {
        if (user.getPortfolio().isLoaded()) return;

        List<String[]> rows = CSVReader.read(TRANSACTIONS_FILE);

        for (String[] row : rows) {
            int userId = Integer.parseInt(row[1]);
            if (userId != user.getUserId()) continue;

            String ticker = row[3];
            double price = Double.parseDouble(row[4].replace(",", "."));
            OrderType orderType = OrderType.fromString(row[6]);
            int quantity = Integer.parseInt(row[7]);

            Stock stock = marketService.findByTicker(ticker);
            if (stock == null) continue;

            Portfolio portfolio = user.getPortfolio();
            Position existing = portfolio.findByTicker(ticker);

            if (orderType == OrderType.BUY) {
                user.deductCash(price * quantity);
                if (existing == null) {
                    portfolio.addPosition(new Position(stock, quantity, price));
                } else {
                    existing.increaseQuantity(quantity, price);
                }
            } else if (orderType == OrderType.SELL) {
                user.addCash(price * quantity);
                if (existing != null) {
                    existing.decreaseQuantity(quantity);
                    if (existing.getQuantity() == 0) {
                        portfolio.removePosition(existing);
                    }
                }
            }
        }

        user.getPortfolio().setLoaded(true);
    }

    /**
     * Returns a complete list of all transactions made by the given user, sorted newest first.
     * <p>
     *     Reads all rows from the transactions CSV, filters on {@code userId}, and parses each
     *     matching row into a {@link Transaction} object. The list is reversed after loading so
     *     the most recently appended transaction appears first.
     * </p>
     *
     * @param user the user whose transaction history is requested
     * @return a {@link List} of {@link Transaction} objects sorted newest first;
     *         never {@code null}, may be empty if the user has made no trades
     */
    public List<Transaction> getTransactionHistory(User user) {
        List<String[]> rows = CSVReader.read(TRANSACTIONS_FILE);
        List<Transaction> history = new ArrayList<>();

        for (String[] row : rows) {
            int userId = Integer.parseInt(row[1]);
            if (userId != user.getUserId()) continue;

            LocalDate date = LocalDate.parse(row[2], DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String ticker = row[3];
            double price = Double.parseDouble(row[4].replace(",", "."));
            String currency = row[5];
            OrderType orderType = OrderType.fromString(row[6]);
            int quantity = Integer.parseInt(row[7]);

            history.add(new Transaction(user.getUserId(), date, ticker, price, currency, orderType, quantity));

        }
        Collections.reverse(history);
        return history;

    }

}