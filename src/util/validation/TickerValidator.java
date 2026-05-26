package util.validation;

import service.StockMarketService;
import util.exception.InvalidAssetException;

/**
 * Utility class for validating stock ticker symbols against the stock market.
 *
 * <p>This class is not meant to be instantiated — all methods are static.</p>
 */
public class TickerValidator {

    /**
     * Validates that the given ticker symbol exists in the stock market.
     *
     * @param ticker  the ticker symbol to look up
     * @param service the {@link StockMarketService} used to search for the stock
     * @throws InvalidAssetException if no stock is found with the given ticker
     */
    public static void validate(String ticker, StockMarketService service) {
        if (service.findByTicker(ticker) == null) {
            throw new InvalidAssetException("No stock found with that ticker: " + ticker);
        }
    }
}