package util.validation;

/**
 * Utility class for validating share quantities.
 *
 * <p>This class is not meant to be instantiated — all methods are static.</p>
 */
public class QuantityValidator {

    /**
     * Validates that the given quantity is greater than zero.
     *
     * @param quantity the number of shares to validate
     * @throws IllegalArgumentException if {@code quantity} is zero or negative
     */
    public static void validate(int quantity) {
        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }
}