package model.asset;

import java.time.LocalDate;

/**
 * Abstract class representing a tradable financial asset.
 * <p>
 *     Contains the fields and behavior shared across all asset types
 *     (e.g. stocks, bonds) Subclasses must provide their own
 * </p>
 */
public abstract class Asset {
    private String ticker;
    private String name;
    private double price;
    private String currency;
    private String rating;
    private String market;
    private LocalDate lastUpdated;

    /**
     * Constructs a new {@code Asset} with the given market data.
     *
     * @param ticker        the unique ticker symbol (e.g. {@code "AAPL"})
     * @param name          the full display name of the asset
     * @param price         the current market price
     * @param currency      the currency code the price is quoted in (e.g. {@code "DKK"})
     * @param rating        the credit or analyst rating
     * @param market        the exchange or market this asset trades on
     * @param lastUpdated   the date the price data was last updated
     */
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

    public String getTicker(){
        return ticker;
    }

    public String getName(){
        return name;
    }

    public double getPrice(){
        return price;
    }

    public String getCurrency(){
        return currency;
    }

    public String getRating(){
        return rating;
    }

    public String getMarket(){
        return market;
    }

    public LocalDate getLastUpdated(){
        return lastUpdated;
    }

    /**
     * Validates that the given price is a positive value.
     *
     * @param price the price to validate
     * @return the price if valid
     * @throws IllegalArgumentException if the price is zero or negative
     */
    private double validatePrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive: " + price);
        }
        return price;
    }
    /**
     * Returns a short readable summary of this asset.
     *
     * @return a formatted summary string
     */
    @Override
    public String toString() {
        return String.format("[%s] %s - %.2f %s", ticker, name, price, currency);
    }
}