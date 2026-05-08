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
    // TODO: declare fields based on the shared columns in stockMarket.csv and bondMarket.csv
    private String ticker;
    private String name;
    private double price;
    private String currency;
    private String rating;
    private String market;
    private LocalDate lastUpdated;
    // Hint: ticker, name, price, currency, rating, market, lastUpdated

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
        // TODO: initialize fields
        this.ticker = ticker;
        this.name = name;
        this.price = price;
        this.currency = currency;
        this.rating = rating;
        this.market = market;
        this.lastUpdated = lastUpdated;
    }

    // TODO: add getters for each field
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
     * Returns a short readable summary of this asset.
     *
     * @return a formatted summary string
     */
    @Override
    public String toString() {
        // TODO: return a readable summary, e.g. "[NOVO-B] Novozymes — 710.00 DKK (Rating: AA)"
        return String.format("[%s] %s - %.2f %s", ticker, name, price, currency);
    }
}