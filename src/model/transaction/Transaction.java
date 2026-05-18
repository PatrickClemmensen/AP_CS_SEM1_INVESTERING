package model.transaction;

import interfaces.CSVSerializable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a single buy or sell transaction made by a club member.
 * <p>
 *     Each transaction is assigned a unique auto-incremented ID and records
 *     the user, date, stock ticker, price, currency, order type and quantity.
 * </p>
 */
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

    /**
     * Constructs a new {@code Transaction} with an auto-incremented ID.
     *
     * @param userId    the ID of the user who made the transaction
     * @param date      the date the transaction was made
     * @param ticker    the ticker symbol of the stock traded
     * @param price     the price per share at the time of the transaction
     * @param currency  the currency code the price is quoted in (e.g. {@code "DKK"})
     * @param orderType the type of order, either {@link OrderType#BUY} or {@link OrderType#SELL}
     * @param quantity  the number of shares traded
     */
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

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public LocalDate getDate() { return date; }
    public String getTicker() { return ticker; }
    public double getPrice() { return price; }
    public String getCurrency() { return currency; }
    public OrderType getOrderType() { return orderType; }
    public int getQuantity() { return quantity; }


    public double getTotalValue() {
        return price * quantity;
    }

    /**
     * Converts this transaction to a semicolon-delimited CSV line.
     * <p>
     *     Column order: {@code id;userId;date;ticker;price;currency;orderType;quantity}
     * </p>
     *
     * @return a CSV-formatted {@code String} representing this transaction
     */
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

    /**
     * Returns a readable summary of this transaction.
     * <p>
     *     Format: {@code [id] orderType | ticker xQuantity @ price currency on date}
     * </p>
     *
     * @return a formatted {@code String} representing this transaction
     */
    @Override
    public String toString() {
        return String.format("[%d] %s | %s x%d @ %.2f %s on %s",
                id, orderType, ticker, quantity, price, currency,
                date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }
}