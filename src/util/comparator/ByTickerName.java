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

    @Override
    public int compare(Position a, Position b) {
        return a.getAsset().getTicker().compareTo(b.getAsset().getTicker());
    }
}