package util.exception;

/**
 * Thrown to indicate that an unsupported or unrecognized curreny code
 * was provided during a currency conversion or validation operation.
 * <p>
 *     This is an unchecked exception, meaning callers are not required
 *     to handle or declare it explicitly.
 * </p>
 **/

public class UnsupportedCurrencyException extends RuntimeException {
    /**
     * Constructs a new {@code UnsupportedCurrencyException}
     *
     * @param message a description of the unsupported currency, typically including
     *                the offending currency code (e.g. {@code "Currency not supported: XYZ}
     */
    public UnsupportedCurrencyException(String message) {
        super(message);
    }
}