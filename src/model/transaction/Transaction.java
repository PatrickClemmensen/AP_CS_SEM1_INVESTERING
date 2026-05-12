package model.transaction;

import interfaces.CSVSerializable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction implements CSVSerializable {
    private static int idCounter = 0;

    private final int id;
    private final int userId;
    private final LocalDate date;
    private final String ticker;
    private final double price;
    private final String currency;
    private final OrderType orderType;
    private final int quantity;

    public Transaction(int userId, LocalDate date, String ticker,
                       double price, String currency, OrderType orderType, int quantity) {
        this.id = ++idCounter;
        this.userId = userId;
        this.date = date;
        this.ticker = ticker;
        this.price = price;
        this.currency = currency;
        this.orderType = orderType;
        this.quantity = quantity;
    }

    /**
     * Sets the static counter to the given value.
     * Call this on startup with the highest existing ID from the CSV
     * so new transactions never collide with existing ones.
     */
    public static void setIdCounter(int value) {
        idCounter = value;
    }

    // --- Getters ---

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public String getTicker() { return ticker; }
    public double getPrice() { return price; }
    public String getCurrency() { return currency; }
    public OrderType getOrderType() { return orderType; }
    public int getQuantity() { return quantity; }

    // --- Calculated ---

    public double getTotalValue() {
        return price * quantity;
    }

    // --- CSV ---

    @Override
    public String toCSVLine() {
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

    // --- toString ---

    @Override
    public String toString() {
        return String.format("[%d] %s | %s x%d @ %.2f %s on %s",
                id, orderType, ticker, quantity, price, currency,
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}