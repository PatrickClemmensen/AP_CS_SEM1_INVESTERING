package util.comparator;

import interfaces.Rankable;
import java.util.Comparator;

/**
 * Compares two {@link Rankable} objects by their percentage return in descending order,
 * so the highest return appears first in a sorted list.
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