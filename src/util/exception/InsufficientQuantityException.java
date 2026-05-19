package util.exception;

/**
 * Thrown when a user attempts to sell more shares of a stock than
 * they currently hold in their portfolio.
 */
public class InsufficientQuantityException extends RuntimeException {
    /**
     * Constructs a new {@code InsufficientQuantityException} with the given message.
     *
     * @param message a description of the error, including the ticker and owned quantity
     */
    public InsufficientQuantityException(String message) {
        super(message);
    }
}
