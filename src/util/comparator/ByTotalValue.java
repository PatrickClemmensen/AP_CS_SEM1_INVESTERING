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

    @Override
    public int compare(User a, User b) {
        return Double.compare(b.getRankValue(), a.getRankValue()); // descending — highest value first
    }
}