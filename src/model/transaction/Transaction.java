package model.transaction;

import interfaces.CSVSerializable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction implements CSVSerializable {
    private final int id;
    private final int userId;
    private final LocalDate date;
    private final String ticker;
    private final double price;
    private final String currency;
    private final OrderType orderType;
    private final int quantity;

    public Transaction(int id, int userId, LocalDate date, String ticker,
                       double price, String currency, OrderType orderType, int quantity){
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.ticker = ticker;
        this.price = price;
        this.currency = currency;
        this.orderType = orderType;
        this.quantity = quantity;
    }

    // TODO: Add getters for id, userId, date, ticker, price, currency, orderType, quantity

    public double getTotalValue() {
        return price * quantity;
    }

    @Override
    public String toCSVLine(){
        return String.join(";",
                String.valueOf(id),
                String.valueOf(userId),
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                ticker,
                String.valueOf(price),
                currency,
                orderType.name().toLowerCase(),
                String.valueOf(quantity));
    }

    // TODO: add toString method
    @Override
    public String toString() {
        return String.format("[%d] %s | %s x%d @ %.2f %s on %s",
                id, orderType, ticker, quantity, price, currency,
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}
