package util.validation;


import util.exception.InvalidInputException;

/**
 * Utility class for validating the initial cash deposit when creating a new portfolio.
 *
 * <p>This class is not meant to be instantiated — all methods are static.</p>
 */
public class InitialInvestmentValidator {

    /**
     * Validates that the initial cash amount meets the minimum requirement of 10,000 DKK.
     * Throws {@link InvalidInputException} if the amount is below the threshold.
     *
     * @param initialCash the initial deposit amount in DKK
     * @throws InvalidInputException if {@code initialCash} is less than 10,000 DKK
     */
    public static void validate(double initialCash) {
        if(initialCash < 10000) {
            throw new InvalidInputException("Initialcash should minimum be 10.000 DKK");
        }
    }
}
