package interfaces;

/**
 * Marks a class as eligible for ranking in a leaderboard.
 * <p>
 *     Classes implementing this interface must provide a numeric value
 *     used to compare and sort instances against each other.
 * </p>
 */
public interface Rankable {

    /**
     * Returns the numeric value used for ranking this object.
     * <p>
     *     A higher value indicates a better ranking position.
     * </p>
     * @return the ranking value as a {@code double}
     */
    double getRankValue();
}