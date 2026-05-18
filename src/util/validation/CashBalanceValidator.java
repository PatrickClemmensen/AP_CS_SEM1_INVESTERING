package util.validation;

import model.portfolio.User;
import util.exception.InsufficientFundsException;

public class CashBalanceValidator {

    public static void validate(User user, double totalCost) {
        // TODO: throw InsufficientFundsException if user.getCashBalance() < totalCost
        if(user.getCashBalance() < totalCost) {
            double shortfall = totalCost - user.getCashBalance();
            throw new InsufficientFundsException(
                    "Insufficient funds. Required : " + totalCost + " DKK | Shortfall: " + shortfall + " DKK");
        }
    }
}