package util.comparator;

import model.portfolio.User;
import java.util.Comparator;

/**
 * Sorts {@link User} objects by total wealth (cash + portfolio value) in descending order.
 * <p>
 *     Used in the leaderboard as an alternative to the default {@link Comparable} sort,
 *     showing absolute DKK value rather than percentage return.
 * </p>
 */
public class ByTotalValue implements Comparator<User> {

    /**
     * Compares two {@link User} objects by total wealth in descending order.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer if {@code b} has less total wealth than {@code a},
     *          zero if equal, or a positive integer if {@code b} has more total wealth
     */
    @Override
    public int compare(User a, User b) {
        return Double.compare(b.getRankValue(), a.getRankValue()); // descending — highest value first
    }
}