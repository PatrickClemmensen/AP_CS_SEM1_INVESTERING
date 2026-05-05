package service;

import model.portfolio.User;
import model.transaction.Transaction;
import model.transaction.OrderType;

public class PortfolioService {
    // TODO: declare a reference to StockMarketService
    // TODO: declare a counter for generating transaction IDs

    public PortfolioService(StockMarketService marketService) {
        // TODO: initialize fields
    }

    public Transaction buy(User user, String ticker, int quantity) {
        // TODO: validate ticker (TickerValidator)
        // TODO: validate quantity (QuantityValidator)
        // TODO: calculate total cost and validate cash balance (CashBalanceValidator)
        // TODO: deduct cash from user
        // TODO: add or update position in user's portfolio
        // TODO: create and return a Transaction with OrderType.BUY
        return null;
    }

    public Transaction sell(User user, String ticker, int quantity) {
        // TODO: validate ticker (TickerValidator)
        // TODO: validate quantity (QuantityValidator)
        // TODO: check user has enough shares (throw InsufficientQuantityException if not)
        // TODO: add cash to user
        // TODO: reduce or remove position from user's portfolio
        // TODO: create and return a Transaction with OrderType.SELL
        return null;
    }
}