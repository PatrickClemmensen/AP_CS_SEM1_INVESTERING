package interfaces;

public interface Tradeable {

    //TODO: return the ticker symbol of the asset
    String getTicker();

    //TODO: return the current market price
    double getPrice();

    //TODO: return the currency code (e.g DKK)
    String getCurrency();
}
