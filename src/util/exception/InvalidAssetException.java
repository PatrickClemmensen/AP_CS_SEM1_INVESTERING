package util.exception;

/**
 * Thrown when a ticker symbol does not match any stock
 * currently listed in the stock market.
 */
public class InvalidAssetException extends RuntimeException {
    /**
     * Constructs a new {@code InvalidAssetException} with the given message.
     *
     * @param message a description of the error, including the unrecognized ticker symbol
     */
    public InvalidAssetException(String message) {
        super(message);
    }
}
