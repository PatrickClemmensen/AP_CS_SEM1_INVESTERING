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
        // TODO: initializ