package model.asset;

import interfaces.CSVSerializable;
import interfaces.Tradeable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a stock listed on a financial market.
 * <p>
 *     Extends {@link Asset} with stock-specific data (sector and dividend yield),
 *     and implements {@link Tradeable} for market operations and
 *     {@link CSVSerializable} for CSV persistence.
 * </p>
 */
public class Stock extends Asset implements Tradeable, CSVSerializable {
    // TODO: declare fields specific to stocks (sector, dividendYield)
    private String sector;
    private double dividendYield;

    // Hint: look at stockMarket.csv column headers

    /**
     * Constructs a new {@code Stock} with the given market data.
     *
     * @param ticker            the unique ticker symbol
     * @param name              the full display name of the stock
     * @param sector            the industry sector (e.g. {@code "Health care"})
     * @param price             the current market price
     * @param currency          the currency code the price is quoted in (e.g. {@code "DKK"}
     * @param rating            the analyst rating
     * @param dividendYield     the annual dividend yield as percentage
     * @param market            the exchange this stock trades on
     * @param lastUpdated       the date the price data was last updated
     */
    public Stock(String ticker, String name, String sector, double price,
                 String currency, String rating, double dividendYield,
                 String market, LocalDate lastUpdated) {
        super(ticker, name, price, currency, rating, market, lastUpdated);
        // TODO: initialize Stock-specific fields
        this.sector = sector;
        this.dividendYield = validateDividendYield(dividendYield);
    }


    /**
     * Returns a semicolon-delimited CSV representation of this stock,
     * matching the column order of {@code stockmatket.csv}
     * <p>
     *     Column order: {@code ticker;name;sector;price;currency;rating;dividendYield;market;lastUpdated}
     * </p>
     *
     * @return a CSV-formatted String representing this stock
     */
    @Override
    public String toCSVLine() {
        // TODO: return semicolon-delimited string matching stockMarket.csv column order
        return String.join(";",
                getTicker(),
                getName(),
                sector,
                String.valueOf(getPrice()),
                getCurrency(),
                getRating(),
                String.valueOf(dividendYield),
                getMarket(),
                getLastUpdated().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    public double validateDividendYield(double dividendYield){
        if (dividendYield < 0) {
            throw new IllegalArgumentException("Dividend Yield must be positive: " + dividendYield);
        }
        return dividendYield;

    }

    // TODO: add getters for sector and dividendYield

    public String getSector(){
        return sector;
    }

    public double getDividendYield(){
        return dividendYield;
    }


    /**
     * Returns a formatted one-line summary of this stock for console display
     * <p>
     *     Columns are padded for alignment in the market table view.
     * </p>
     *
     * @return a formatted string showing ticker, name, sector, price and currency
     */
    @Override
    public String toString(){
        return String.format("%-10s %-40s %-22s %16.2f %8s", getTicker(), getName(), getSector(), getPrice(), getCurrency());

    }

}