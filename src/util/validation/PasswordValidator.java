package util.validation;

import util.AppConstants;
import util.exception.InvalidInputException;

/**
 * Utility class for validating the leader password.
 *
 * <p>This class is not meant to be instantiated — all methods are static.</p>
 */
public class PasswordValidator {

    /**
     * Validates the provided password against the leader password defined in {@link AppConstants}.
     * Throws {@link InvalidInputException} if the input is empty or incorrect.
     *
     * @param input the raw password input from the user
     * @return {@code true} if the password is correct
     * @throws InvalidInputException if the input is empty or does not match the leader password
     */
    public static boolean validatePassword(String input) {
        String password = input.trim();
        if (password.isEmpty()) {
            throw new InvalidInputException("Password cannot be empty.");
        }
        if (!password.equals(AppConstants.LEADER_PASSWORD)) {
            throw new InvalidInputException("Access Denied.");
        }
        return true;
    }
}
