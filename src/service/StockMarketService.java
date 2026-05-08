package service;

import model.asset.Stock;
import util.constants.Colors;
import util.csv.CSVReader;
import util.printing.ConsolePrinter;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for loading and querying the stock market.
 * <p>
 *     Stocks are read from CSV file at startup and stored in memory,
 *     indexed by their ticker symbol for last lookup
 * </p>
 */
public class StockMarketService {
    // TODO: declare a Map to store stocks by ticker
    private final Map<String, Stock> stockMap = new HashMap<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Constructs a new {@code StockMarketServie} and loads stock data from the given CSV file.
     *
     * @param stockFilePath the path to the CSV file containing stock market data.
     */
    public StockMarketService(String stockFilePath) {
        // TODO: call a private load() method
        load(stockFilePath);
    }

    /**
     * Reads and parses stock data from the specified CSV file into {@link #stockMap}.
     * <p>
     *     Each row in the CSV file is expected to follow this column order:
     *     {@code ticker;name;sector;price;currency;rating;dividend_yield;market;last_updated}
     * </p>
     * <p>
     *     Decimal commas are normalized to decimal points before parsing.
     * </p>
     * @param path the file path to the CSV file to load
     */
    private void load(String path) {
        // TODO: use CSVReader to read stockMarket.csv
        for(String[] row : CSVReader.read(path)){
        // TODO: parse each row into a Stock object
            String ticker = row[0].trim();
            String name = row[1].trim();
            String sector = row[2].trim();
            double price = Double.parseDouble(row[3].trim().replace(",","."));
            String currency = row[4].trim();
            String rating = row[5].trim();
            double dividendYield = Double.parseDouble(row[6].trim().replace(",","."));
            String market = row[7].trim();
            LocalDate lastUpdated = LocalDate.parse(row[8].trim(), FORMATTER);

            // TODO: put each Stock into the map using its ticker as key
            Stock stock = new Stock(ticker, name, sector, price, currency, rating, dividendYield, market, lastUpdated);
            stockMap.put(ticker, stock);
        }


        // Hint: row order is ticker;name;sector;price;currency;rating;dividend_yield;market;last_updated
    }

    /**
     * Finds and returns the {@link Stock} associated with the given ticker symbol
     * <p>
     *     The lookup is case-insensitive - the ticker is converted to uppercase before searching
     * </p>
     * @param ticker the ticker symbol to search for (e.g. {@code "AAPL"} or {@code "aapl"
     * @return the matching {@link Stock}, or {@code null} if no stock with that ticker exists
     */
    public Stock findByTicker(String ticker) {
        // TODO: return the Stock for the given ticker, or null if not found
        return stockMap.get(ticker.toUpperCase());
    }

    /**
     * Returns all stocks currently loaded in the market.
     *
     * @return a {@link Collection} of all {@link Stock} objects; never {@code null}
     */
    public Collection<Stock> getAllStocks() {
        // TODO: return all stocks in the map
        return stockMap.values();
    }


    /**
     * Prints a formatted table of all available stocks to the console
     * <p>
     *     Displayed a header row followed by one row per stock, showing
     *     ticker, name, sector, current price and currency
     *     Each stock row is formatted using {@link Stock#toString()}.
     * </p>
     */
    public void viewMarket(){
        ConsolePrinter.printMenuHeader("\n============================== STOCK MARKET ===============================\n");
        System.out.printf("%-10s %-30s %-14s %11s %6s%n", "TICKER", "NAME", "SECTOR", "PRICE", "CURR");
        //System.out.println("-".repeat(75));
        ConsolePrinter.printSeparator();

        for(Stock stock : stockMap.values()){
            System.out.println(Colors.MENUOPTION + stock + Colors.RESET);
        }
    }
}