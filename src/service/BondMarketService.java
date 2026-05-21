package service;

import model.asset.Bond;
import util.csv.CSVReader;
import util.printing.ConsolePrinter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for loading and querying the bond market.
 * <p>
 *     Bonds are read from a CSV file at startup and stored in memory,
 *     indexed by their ticker symbol for fast lookup.
 * </p>
 */
public class BondMarketService {
    private final Map<String, Bond> bondMap = new HashMap<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructs a new {@code BondMarketService} and loads bond data from the given CSV file.
     *
     * @param bondFilePath the path to the CSV file containing bond market data
     */
    public BondMarketService(String bondFilePath) {
        load(bondFilePath);
    }

    /**
     * Reads and parses bond data from the CSV file into {@link #bondMap}.
     * <p>
     *     Column order: {@code ticker;name;price;currency;coupon_rate;issue_date;maturity_date;rating;market;last_updated}
     * </p>
     *
     * @param path the file path to load
     */
    private void load(String path) {
        for (String[] row : CSVReader.read(path)) {
            String ticker          = row[0].trim();
            String name            = row[1].trim();
            double price           = Double.parseDouble(row[2].trim().replace(",", "."));
            String currency        = row[3].trim();
            double couponRate      = Double.parseDouble(row[4].trim().replace(",", "."));
            LocalDate issueDate    = LocalDate.parse(row[5].trim(), FORMATTER);
            LocalDate maturityDate = LocalDate.parse(row[6].trim(), FORMATTER);
            String rating          = row[7].trim();
            String market          = row[8].trim();
            LocalDate lastUpdated  = LocalDate.parse(row[9].trim(), FORMATTER);

            bondMap.put(ticker, new Bond(ticker, name, price, currency, couponRate,
                    issueDate, maturityDate, rating, market, lastUpdated));
        }
    }

    /**
     * Finds and returns the {@link Bond} associated with the given ticker symbol.
     *
     * @param ticker the ticker symbol to search for
     * @return the matching {@link Bond}, or {@code null} if not found
     */
    public Bond findByTicker(String ticker) {
        return bondMap.get(ticker.toUpperCase());
    }

    /**
     * Returns all bonds currently loaded in the market.
     *
     * @return a {@link Collection} of all {@link Bond} objects
     */
    public Collection<Bond> getAllBonds() {
        return bondMap.values();
    }

    /**
     * Prints a formatted table of all available bonds to the console.
     */
    public void viewMarket() {
        ConsolePrinter.printMenuTitle("──────────────────────────────────────────── Bond Market ──────────────────────────────────────────");
        System.out.printf("%-10s %-40s %10s %16s %8s %-15s%n",
                "TICKER", "NAME", "COUPON %", "PRICE", "CURR", "MATURES");
        ConsolePrinter.printSeparator();
        for (Bond bond : bondMap.values()) {
            System.out.println(bond);
        }
    }
}