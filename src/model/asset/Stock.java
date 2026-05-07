package model.asset;

import interfaces.CSVSerializable;
import interfaces.Tradeable;

import java.time.LocalDate;

public class Stock extends Asset implements Tradeable, CSVSerializable {
    // TODO: declare fields specific to stocks (sector, dividendYield)
    // Hint: look at stockMarket.csv column headers

    public Stock(String ticker, String name, String sector, double price,
                 String currency, String rating, double dividendYield,
                 String market, LocalDate lastUpdated) {
        super(ticker, name, price, currency, rating, market, lastUpdated);
        // TODO: initialize Stock-specific fields
    }

    // TODO: add getters for sector and dividendYield


    @Override
    public String toCSVLine() {
        // TODO: return semicolon-delimited string matching stockMarket.csv column order
        return null;
    }

    @Override
    public String getTicker() {
        return "";
    }

    @Override
    public String getName(){
        return "";
    }


    @Override
    public double getPrice(