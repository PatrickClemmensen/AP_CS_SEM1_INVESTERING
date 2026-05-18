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

    @Override
    public int compare(Rankable a, Rankable b) {
        return Double.compare(getReturn(b), getReturn(a)); // descending
    }

    private double getReturn(Rankable rankable) {
        if (rankable instanceof User user) {
            return ((user.getRankValue() - user.getInitialCash()) / user.getInitialCash()) * 100;
        }
        // for Position, getRankValue() already returns percent return
        return rankable.getRankValue();
    }
}