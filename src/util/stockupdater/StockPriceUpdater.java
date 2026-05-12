package util.stockupdater;

import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class StockPriceUpdater {

    public static void updatePricesForDate(int year, int month, int day) throws IOException {
        String snapshotPath = String.format("data/snapshots/stockMarket_%02d-%02d-%04d.csv", day, month, year);
        String targetPath = "data/stockMarket.csv";

        // Just copy the snapshot over the active file
        Files.copy(Path.of(snapshotPath), Path.of(targetPath), StandardCopyOption.REPLACE_EXISTING);

    }
}