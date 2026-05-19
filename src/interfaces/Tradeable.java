package interfaces;

/**
 * Defines a contract for assets that can be traded on the stock market.
 * <p>
 *     Classes implementing this interface represent tradeable financial
 *     instruments such as stock or bonds, and must expose the core
 *     market data required to execute trades.
 * </p>
 */
public interface Tradeable {

    /**
     * Returns the ticker symbol of this asset.
     *
     * @return th ticker symbol (e.g. {@code "AAPL"})
     */
    String getTicker();

    /**
     * Returns the current market price of this asseet.
     *
     * @return the current price as a {@code double}
     */
    double getPrice();

    /**
     * Returns the currency code this asset is priced in.
     *
     * @return the currency code (e.g. {@code "DKK"}
     */
    String getCurrency();
}
