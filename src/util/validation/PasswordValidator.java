package util.validation;

import util.AppConstants;
import util.exception.InvalidInputException;

public class PasswordValidator {

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
