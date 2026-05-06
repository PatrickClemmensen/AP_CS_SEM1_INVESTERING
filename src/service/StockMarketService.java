package service;

import model.asset.Stock;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StockMarketService {
    // TODO: declare a Map to store stocks by ticker
    private final Map<String, Stock> stockMap = new HashMap<>();


    public StockMarketService(String stockFilePath) {
        // TODO: call a private load() method
        load(stockFilePath);
    }

    private void load(String path) {
        // TODO: use CSVReader to read stockMarket.csv
        // TODO: parse each row into a Stock object
        // TODO: put each Stock into the map using its ticker as key
        // Hint: row order is ticker;name;sector;price;currency;rating;dividend_yield;market;last_updated
    }

    public Stock findByTicker(String ticker) {
        // TODO: return the Stock for the given ticker, or null if not found
        return stockMap.get(ticker.toUpperCase());
    }

    public Collection<Stock> getAllStocks() {
        // TODO: return all stocks in the map
        return stockMap.values();
    }
}