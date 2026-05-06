package model.asset;

public abstract class Asset {
    // TODO: declare fields based on the shared columns in stockMarket.csv and bondMarket.csv
    // Hint: ticker, name, price, currency, rating, market, lastUpdated

    public Asset(String ticker, String name, double price, String currency,
                 String rating, String market, String lastUpdated) {
        // TODO: initialize fields
    }

    // TODO: add getters for each field

    @Override
    public String toString() {
        // TODO: return a readable summary, e.g. "[NOVO-B] Novozymes — 710.00 DKK (Rating: AA)"
        return null;
    }
}