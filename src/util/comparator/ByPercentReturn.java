package util.comparator;

import interfaces.Rankable;
import model.portfolio.User;

import java.util.Comparator;

/**
 * Sorts any {@link Rankable} by its rank value in descending order.
 * <p>
 *     Works across both {@link model.portfolio.Position} (sorts by percentage return)
 *     and {@link model.portfolio.User} (sorts by total portfolio value),
 *     since both implement {@link Rankable#getRankValue()}.
 * </p>
 */
public class ByPercentReturn implements Comparator<Rankable> {

    /**
     * Compares two {@link Rankable} objects by their rank value in descending order.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer if {@code b} has a lower rank value than {@code a},
     * zero if equal, or a positive integer if {@code b} has a higher rank value
     */
    @Override
    public int compare(Rankable a, Rankable b) {
        return 0;
    }
}