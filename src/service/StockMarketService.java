package service;

import model.asset.Stock;
import util.constants.Colors;
import util.csv.CSVReader;
import util.printing.ConsolePrinter;

import java.util.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Service responsible for loading and querying the stock market.
 * <p>
 *     Stocks are read from CSV file at startup and stored in memory,
 *     indexed by their ticker symbol for last lookup
 * </p>
 */
public class StockMarketService {
    private final Map<String, Stock> stockMap = new HashMap<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Constructs a new {@code StockMarketServie} and loads stock data from the given CSV file.
     *
     * @param stockFilePath the path to the CSV file containing stock market data.
     */
    public StockMarketService(String stockFilePath) {
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
        for(String[] row : CSVReader.read(path)){
            String ticker = row[0].trim();
            String name = row[1].trim();
            String sector = row[2].trim();
            double price = Double.parseDouble(row[3].trim().replace(",","."));
            String currency = row[4].trim();
            String rating = row[5].trim();
            double dividendYield = Double.parseDouble(row[6].trim().replace(",","."));
            String market = row[7].trim();
            LocalDate lastUpdated = LocalDate.parse(row[8].trim(), FORMATTER);

            Stock stock = new Stock(ticker, name, sector, price, currency, rating, dividendYield, market, lastUpdated);
            stockMap.put(ticker, stock);
        }

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
        return stockMap.get(ticker.toUpperCase());
    }

    /**
     * Returns all stocks currently loaded in the market.
     *
     * @return a {@link Collection} of all {@link Stock} objects; never {@code null}
     */
    public Collection<Stock> getAllStocks() {
        return stockMap.values();
    }

    /**
     * Searches the stock market for stocks matching the given input.
     * <p>
     *     The search is case-insensitive against ticker symbol,
     *     company name and sector. A Stock is included in the results if any
     *     of these fields contain the search input as a substring.
     * </p>
     *
     * @param searchInput the search string entered by the user
     * @return a {@link Collection} of {@link Stock} objects matching the search input;
     *          never a {@code null}, may be empty if no matches are found
     */
    public Collection<Stock> searchStocks(String searchInput) {
        String search = searchInput.trim().toLowerCase();

        Collection<Stock> results = new ArrayList<>();

        for (Stock stock : stockMap.values()) {
            if (stock.getTicker().toLowerCase().contains(search) ||
                    stock.getName().toLowerCase().contains(search) ||
                    stock.getSector().toLowerCase().contains(search)) {
                results.add(stock);
            }
        }
        return results;
    }

    /**
     * Prints a formatted table of all available stocks to the console
     * <p>
     *     Displayed a header row followed by one row per stock, showing
     *     ticker, name, sector, current price and currency
     *     Each stock row is formatted using {@link Stock#toString()}.
     * </p>
     */
    public void viewMarket() {
        viewMarket(stockMap.values());
    }

    /**
     * Prints a formatted table of the given stocks to the console.
     * <p>
     *     Used to display either the full market or a filtered subset of stocks,
     *     such as search results. Each stock row is formatted using {@link Stock#toString()}.
     * </p>
     *
     * @param stocks the collection of stocks to display
     */
    public void viewMarket(Collection<Stock> stocks){
        ConsolePrinter.printMenuTitle("──────────────────────────────────────────── Stock Market ─────────────────────────────────────────");
        System.out.printf("%-10s %-40s %-22s %16s %8s%n", "TICKER", "NAME", "SECTOR", "PRICE", "CURR");
        ConsolePrinter.printSeparator();

        for(Stock stock : stocks){
            System.out.println(Colors.MENUOPTION + stock + Colors.RESET);
        }
    }
}