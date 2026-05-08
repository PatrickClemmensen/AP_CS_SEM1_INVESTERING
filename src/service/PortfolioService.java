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

import java.util.List;

public class PortfolioService {
    // TODO: declare a reference to StockMarketService
    private StockMarketService marketService;
    // TODO: declare an int counter for generating transaction IDs

    public PortfolioService(StockMarketService marketService) {
        // TODO: initialize fields
        this.marketService = marketService;
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
        return null;
    }

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
                if(existing == null){
                    portfolio.addPosition(new Position(stock, quantity,price));
                } else {
                    existing.increaseQuantity(quantity, price);
                }
            } else if (orderType == OrderType.SELL){
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