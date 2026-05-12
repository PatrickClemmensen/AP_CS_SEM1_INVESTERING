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
import util.exception.InsufficientQuantityException;
import java.time.LocalDate;

import java.util.List;

public class PortfolioService {
    // TODO: declare a reference to StockMarketService
    private StockMarketService marketService;
    // TODO: declare an int counter for generating transaction IDs

    public PortfolioService(StockMarketService marketService) {
        // TODO: initialize fields
        this.marketService = marketService;
        //this.transactionCounter = 0;
    }

    public Transaction buy(User user, String ticker, int quantity) {
        // TODO: validate ticker (TickerValidator)
        // TODO: validate quantity (QuantityValidator)
        // TODO: calculate total cost: stock.getPrice() * quantity
        // TODO: validate cash balance (CashBalanceValidator)
        // TODO: deduct total cost from user cash (user.deductCash())

        // TODO: check if the user already owns this stock (portfolio.findByTicker())
        //   If YES — recalculate weighted average buy price:
        //     newAverage = (existingQty * existingAvg + newQty * newPrice) / (existingQty + newQty)
        //     then call position.setAverageBuyPrice(newAverage) and position.addQuantity(quantity)
        //   If NO — create a new Position with averageBuyPrice set to current stock price
        //     and add it to the portfolio (portfolio.addPosition())

        // TODO: create and return a Transaction with OrderType.BUY
        return null;
    }

    public Transaction sell(User user, String ticker, int quantity) {
        //Find the stock in the market to get the current price
        Stock stock = marketService.findByTicker(ticker);

        //Get the user's portfolio and find their position in this stock
        Portfolio portfolio = user.getPortfolio();
        Position position = portfolio.findByTicker(ticker);

        //Safety check
        if (position == null || position.getQuantity() < quantity) {
            throw new InsufficientQuantityException("Insufficient quantity for ticker " + ticker);
        }

        //Calculate how much cash the user receives from the sale
        double totalValue = stock.getPrice() * quantity;

        //Credit the cash to the user's balance
        user.addCash(totalValue);

        //Reduce the position by the sold quantity
        position.decreaseQuantity(quantity);


        //If they sold everything, remove the position from the portfolio entirely
        if (position.getQuantity() == 0) {
            portfolio.removePosition(position);
        }

        //Build and return the transaction record
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


        // TODO: validate ticker (TickerValidator)
        // TODO: validate quantity (QuantityValidator)
        // TODO: check user owns this stock (portfolio.findByTicker())
        //   If position is null or position.getQuantity() < quantity
        //   throw InsufficientQuantityException

        // TODO: calculate total value: stock.getPrice() * quantity
        // TODO: add total value to user cash (user.addCash())

        // TODO: reduce position quantity (position.addQuantity(-quantity))
        //   If quantity reaches zero, remove the position from the portfolio entirely
        //   Hint: add a removePosition(String ticker) method to Portfolio

        // TODO: create and return a Transaction with OrderType.SELL



    public void loadPortfolio(User user){
        List<String[]> rows = CSVReader.read(AppConstants.TRANSACTIONS_FILE);

        for(String[] row : rows){
            int userId = Integer.parseInt(row[1]);
            if(userId != user.getUserId()) continue;

            String ticker = row[3];
            double price = Double.parseDouble(row[4].replace(",", "."));
            OrderType orderType = OrderType.fromString(row[6]);
            int quantity = Integer.parseInt(row[7]);

            Stock stock = marketService.findByTicker(ticker);
            if(stock == null) continue;

            Portfolio portfolio = user.getPortfolio();
            Position existing = portfolio.findByTicker(ticker);

            if(orderType == OrderType.BUY){
                user.deductCash(price * quantity);
                if(existing == null){
                    portfolio.addPosition(new Position(stock, quantity,price));
                } else {
                    existing.increaseQuantity(quantity, price);
                }
            } else if (orderType == OrderType.SELL){
                user.addCash(price*quantity);
                if(existing != null){
                    existing.decreaseQuantity(quantity);
                    if(existing.getQuantity() == 0){
                        portfolio.removePosition(existing);
                    }
                }
            }
        }
    }

}