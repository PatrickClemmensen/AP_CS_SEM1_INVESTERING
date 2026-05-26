package util.comparator;

import model.portfolio.Position;
import java.util.Comparator;

/**
 * Sorts {@link Position} objects alphabetically by ticker symbol.
 * <p>
 *     Used in the portfolio view as an alternative to the default {@link Comparable} sort,
 *     which orders positions by percentage return.
 * </p>
 */
public class ByTickerName implements Comparator<Position> {


    /**
     * Compares two {@link Position} objects alphabetically by ticker symbol.
     *
     * @param a the first object to be compared.
     * @param b the second object to be compared.
     * @return a negative integer if {@code a} comes before {@code b} alphabetically,
     *          zero if equal, or a positive integer if {@code a} comes after {@code b}
     */
    @Override
    public int compare(Position a, Position b) {
        return a.getAsset().getTicker().compareTo(b.getAsset().getTicker());
    }
}