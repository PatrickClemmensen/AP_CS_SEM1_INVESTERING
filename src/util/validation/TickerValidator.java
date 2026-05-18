package util.validation;

import service.StockMarketService;
import util.exception.InvalidAssetException;

public class TickerValidator {

    public static void validate(String ticker, StockMarketService service) {
        // TODO: check if the ticker exists in the market service
        if (service.findByTicker(ticker) == null) {
            throw new InvalidAssetException("No stock found with that ticker: " + ticker);
        }
    }
}