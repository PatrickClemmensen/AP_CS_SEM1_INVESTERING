package service;

import model.asset.Stock;
import model.portfolio.Portfolio;
import model.portfolio.Position;
import model.portfolio.User;
import model.transaction.Transaction;
import model.transaction.OrderType;
import util.constants.AppConstants;
import util.csv.CSVReader;
import util.exception.InsufficientFundsException;
import util.exception.InsufficientQuantityException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class PortfolioService {

    private StockMarketService marketService;

    public PortfolioService(StockMarketService marketService) {
        this.marketService = marketService;
    }

    public Transaction buy(User user, String ticker, int quantity) {
        Stock stock = marketService.findByTicker(ticker);
        if (stock == null) {
            throw new IllegalArgumentException("Stock not found: " + ticker);
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        double totalCost = stock.getPrice() * quantity;

        if (user.getCashBalance() < totalCost) {
            throw new InsufficientFundsException("Insufficient funds. Required: " + totalCost
                    + " Available: " + user.getCashBalance());
        }

        user.deductCash(totalCost);

        Position existing = user.getPortfolio().findByTicker(ticker);
        if (existing != null) {
            existing.increaseQuantity(quantity, stock.getPrice());
        } else {
            user.getPortfolio().addPosition(new Position(stock, quantity, stock.getPrice()));
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

    public Transaction sell(User user, String ticker, int quantity) {
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

    public void loadPortfolio(User user) {
        loadPortfolio(user, AppConstants.TRANSACTIONS_FILE);
    }

    public void loadPortfolio(User user, String transactionsFile) {
        if (user.getPortfolio().isLoaded()) return;

        List<String[]> rows = CSVReader.read(transactionsFile);

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

    public void resetPortfolios(Collection<User> users) {
        for (User user : users) {
            user.getPortfolio().getPositions().clear();
            user.getPortfolio().setLoaded(false);
            user.setCashBalance(user.getInitialCash());
        }
    }
}