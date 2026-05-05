package service;

import model.portfolio.User;
import model.transaction.Transaction;
import model.transaction.OrderType;

public class PortfolioService {
    // TODO: declare a reference to StockMarketService
    // TODO: declare an int counter for generating transaction IDs

    public PortfolioService(StockMarketService marketService) {
        // TODO: initialize fields
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
}