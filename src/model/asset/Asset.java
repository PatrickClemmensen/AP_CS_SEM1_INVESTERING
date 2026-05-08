package model.asset;

import java.time.LocalDate;

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
    public String getName(){
        return name;
    }


    @Override
    public String toString() {
        // TODO: return a readable summary, e.g. "[NOVO-B] Novozymes — 710.00 DKK (Rating: AA)"
        return null;
    }
}