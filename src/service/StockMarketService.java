package service;

import model.asset.Stock;
import util.constants.Colors;
import util.csv.CSVReader;
import util.printing.ConsolePrinter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class StockMarketService {
    private final Map<String, Stock> stockMap = new HashMap<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public StockMarketService(String stockFilePath) {
        load(stockFilePath);
    }

    private void load(String path) {
        for (String[] row : CSVReader.read(path)) {
            try {
                String ticker = row[0].trim();
                String name = row[1].trim();
                String sector = row[2].trim();
                double price = Double.parseDouble(row[3].trim().replace(",", "."));
                String currency = row[4].trim();
                String rating = row[5].trim();
                double dividendYield = Double.parseDouble(row[6].trim().replace(",", "."));
                String market = row[7].trim();
                LocalDate lastUpdated = LocalDate.parse(row[8].trim(), FORMATTER);

                Stock stock = new Stock(ticker, name, sector, price, currency, rating, dividendYield, market, lastUpdated);
                stockMap.put(ticker, stock);
            } catch (Exception e) {
                System.out.println("Failed to load row: " + String.join(";", row) + " → " + e.getMessage());
            }
        }
    }

    public void reload(String stockFilePath) {
        stockMap.clear();
        load(stockFilePath);
    }

    public Stock findByTicker(String ticker) {
        return stockMap.get(ticker.toUpperCase());
    }

    public Collection<Stock> getAllStocks() {
        return stockMap.values();
    }

    public void viewMarket() {
        ConsolePrinter.printMenuHeader("\n=========================================== STOCK MARKET ===========================================\n");
        System.out.printf("%-10s %-40s %-22s %16s %8s%n", "TICKER", "NAME", "SECTOR", "PRICE", "CURR");
        ConsolePrinter.printSeparator();
        for (Stock stock : stockMap.values()) {
            System.out.println(Colors.MENUOPTION + stock + Colors.RESET);
        }
    }
}