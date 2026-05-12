// src/util/AppConstants.java
package util.constants;

public class AppConstants {
    private AppConstants() {} // prevent instantiation

    // File paths
    public static final String STOCK_MARKET_FILE  = "data/stockMarket.csv";
    public static final String BOND_MARKET_FILE   = "data/bondMarket.csv";
    public static final String USERS_FILE         = "data/users.csv";
    public static String TRANSACTIONS_FILE  = "data/transactions.csv";
    public static final String CURRENCY_FILE      = "data/currency.csv";

    // CSV
    public static final String CSV_DELIMITER      = ";";
    public static final String DECIMAL_SEPARATOR  = ",";

    // Currency
    public static final String BASE_CURRENCY      = "DKK";

    // Club Leader Password
    public static final String LEADER_PASSWORD = "admin";
}