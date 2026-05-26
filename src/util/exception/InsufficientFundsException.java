package util.exception;

/**
 * Thrown when a user attempts to purchase stocks without sufficient funds
 * to cover the total cost of the transaction.
 */
public class InsufficientFundsException extends RuntimeException {
    /**
     * Constructs a new {@code InsufficientFundsException} with the given message.
     *
     * @param message a description of the error, including the required amount and shortfall
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}
