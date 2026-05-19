package util.validation;

import model.portfolio.User;
import util.exception.InsufficientFundsException;

/**
 * Utility class for validating that a user has sufficient cash balance
 * to cover a purchase.
 *
 * <p>
 *     This class is not meant to be instantiated — all methods are static.
 * </p>
 */
public class CashBalanceValidator {

    /**
     * Validates that the user's cash balance covers the total cost.
     * Throws {@link InsufficientFundsException} if the balance is insufficient,
     * including the shortfall amount in the message.
     *
     * @param user      the user whose cash balance is checked
     * @param totalCost the required amount in DKK
     * @throws InsufficientFundsException if the user's balance is less than {@code totalCost}
     */
    public static void validate(User user, double totalCost) {
        if(user.getCashBalance() < totalCost) {
            double shortfall = totalCost - user.getCashBalance();
            throw new InsufficientFundsException(
                    "Insufficient funds. Required : " + totalCost + " DKK | Shortfall: " + shortfall + " DKK");
        }
    }
}