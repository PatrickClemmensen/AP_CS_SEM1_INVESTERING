package model.portfolio;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a member's investment portfolio - a collection of {@link Position} objects,
 * each describing how many shares of a given stock the member holds.
 * <p>
 *     The portfolio is loaded lazily from the transactions CSV the first time it is needed.
 *     The {@code loaded} flag prevents duplicate loading within the same session.
 * </p>
 *
 * @see Position
 */
public class Portfolio {
    private List<Position> portfolio = new ArrayList<>();
    private boolean loaded = false;

    /**
     * Returns whether this portfolio has already been populated from persistent storage.
     *
     * @return {@code true} if the portfolio has been loaded; {@code false} otherwise
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Sets the loaded state of this portfolio.
     * <p>
     *     Set to {@code true} after the portfolio has been successfully read from the
     *     transaction CSV to prevent duplicate loading within the same session.
     * </p>
     *
     * @param loaded {@code true} to mark the portfolio as loaded; {@code false} to reset it
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Adds a new {@link Position} to this portfolio.
     *
     * @param position the position to add
     */
    public void addPosition(Position position) {
        portfolio.add(position);
    }

    /**
     * Removes an existing {@link Position} from this portfolio.
     *
     * @param position the position to remove
     */
    public void removePosition(Position position) {
        portfolio.remove(position);
    }

    /**
     * Returns all positions currently held in this portfolio.
     *
     * @return a {@link List} of {@link Position} objects; never {@code null}, may be empty
     */
    public List<Position> getPositions() {
        return portfolio;
    }

    /**
     * Finds and retuns the {@link Position} of the given ticker symbol.
     *
     * @param ticker the ticker symbol to search for (case-sensitive)
     * @return the matching {@link Position}, or {@code null} if no position with that ticker exists
     */
    public Position findByTicker(String ticker) {
        return portfolio.stream()
                .filter(p -> p.getAsset().getTicker().equals(ticker))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns the total current market value of all positions in this portfolio, in DKK.
     * <p>
     *     Calculated as the sum of {@link Position#getCurrentValue()} across all positions.
     *     Returns {@code 0.0} if the portfolio is empty.
     * </p>
     *
     * @return total market value in DKK
     */
    public double getTotalValue() {
        return portfolio.stream()
                .mapToDouble(Position::getCurrentValue)
                .sum();
    }

    /**
     * Returns the total unrealized gain or loss across all positions in this portfolio, in DKK.
     * <p>
     *     Calculated as the sum of {@link Position#getUnrealizedGain()} across all positions.
     *     A positive value indicates an overall gain; a negative value indicates an overall loss.
     *     Returns {@code 0.0} if the portfolio is empty.
     * </p>
     *
     * @return total unrealized gain/loss in DKK
     */
    public double getTotalGain() {
        return portfolio.stream()
                .mapToDouble(Position::getUnrealizedGain)
                .sum();
    }
}