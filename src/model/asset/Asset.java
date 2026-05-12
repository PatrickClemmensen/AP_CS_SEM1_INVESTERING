package model.asset;

import java.time.LocalDate;

public abstract class Asset {
    private String ticker;
    private String name;
    private double price;
    private String currency;
    private String rating;
    private String market;
    private LocalDate lastUpdated;

    public Asset(String ticker, String name, double price, String currency,
                 String rating, String market, LocalDate lastUpdated) {
        this.ticker = ticker;
        this.name = name;
        this.price = validatePrice(price);
        this.currency = currency;
        this.rating = rating;
        this.market = market;
        this.lastUpdated = lastUpdated;
    }

    public String getTicker() { return ticker; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getCurrency() { return currency; }
    public String getRating() { return rating; }
    public String getMarket() { return market; }
    public LocalDate getLastUpdated() { return lastUpdated; }

    private double validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive: " + price);
        }
        return price;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f %s", ticker, name, price, currency);
    }
}