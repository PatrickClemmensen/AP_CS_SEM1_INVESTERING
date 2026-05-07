package service;

import model.asset.Stock;
import util.constants.Colors;
import util.csv.CSVReader;

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
     