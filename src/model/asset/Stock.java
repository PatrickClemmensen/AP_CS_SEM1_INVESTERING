package model.asset;

import interfaces.CSVSerializable;
import interfaces.Tradeable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Stock extends Asset implements Tradeable, CSVSerializable {
    // TODO: declare fields specific to stocks (sector, dividendYield)
    private String ticker;
    private String name;
    private String sector;
    private double price;
    private String currency;
    private String rating;
    private double dividendYield;
    private String market;
    private LocalDate lastUpdated;
    // Hint: look at stockMarket.csv column headers

    public Stock(String ticker, String name, String sector, double price,
                 String currency, String rating, double dividendYield,
                 String market, LocalDate lastUpdated) {
        super(ticker, name, price, currency, rating, market, lastUpdated);
        // TODO: initialize Stock-specific fields
        this.sector = sector;
        this.dividendYield = dividendYield;
    }


    @Override
    public String toCSVLine() {
        // TODO: return semicolon-delimited string matching stockMarket.csv column order
        return String.join(";",
                ticker,
                name,
                sector,
                String.valueOf(price),
                currency,
                rating,
                String.valueOf(dividendYield),
                market,
                lastUpdated.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    // TODO: add getters for sector and dividendYield

    public String getSector(){
        return sector;
    }

    public double getDividedYield(){
        return dividendYield;
    }

    @Override
    public String getTicker() {
        return ticker;
    }

    @Override
    public String getName(){
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getCurrency() {
        return currency;
    }


    @Override
    public String toString(){
        return String.format("%-8s %-30s %-15s %10.2f %6s", getTicker(), getName(), getSector(), getPrice(), getCurrency());

    }
}