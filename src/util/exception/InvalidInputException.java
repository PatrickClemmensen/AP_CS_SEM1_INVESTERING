package util.exception;

/**
 * Thrown when user input fails validation, such as an incorrect
 * password or an unrecognized menu choice.
 */
public class InvalidInputException extends RuntimeException {
    /**
     * Creates a new InvalidInputException with the given message.
     *
     * @param message the detail message explaining why the input is invalid
     */
    public InvalidInputException(String message) {
        super(message);
    }
}
