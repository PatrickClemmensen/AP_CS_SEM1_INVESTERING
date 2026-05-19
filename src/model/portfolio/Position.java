package model.portfolio;

import interfaces.CSVSerializable;
import interfaces.Rankable;
import interfaces.Tradeable;
import model.asset.Asset;
import util.constants.Colors;

/**
 * Represents a single stock position held in a member's portfolio.
 * <p>
 *     A position tracks how many shares of a given {@link Asset} the member holds,
 *     at what average purchase price, and calculates current value and unrealized gain/loss.
 * </p>
 * <p>
 *     The average purchase prise is recalculated on every additional purchase using a
 *     weighted average, so it always reflects the true cost basis across multiple purchases.
 * </p>
 *
 * @see Portfolio
 * @see Asset
 */
public class Position implements Rankable, CSVSerializable, Comparable<Position> {
    private final Asset asset;
    private int quantity;
    private double averageBuyPrice;

    /**
     * Constructs a new {@code postition} for the given asset.
     *
     * @param asset             the stock this position represents
     * @param quantity          the number of shares initially purchased
     * @param averageBuyPrice   the price per share paid at the time of purchase, in DKK
     */
    public Position(Asset asset, int quantity, double averageBuyPrice) {
        this.asset = asset;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
    }

    public Asset getAsset() { return asset; }
    public int getQuantity() { return quantity; }
    public double getAverageBuyPrice() { return averageBuyPrice; }

    /**
     * Increases the position size with an additional purchase and recalculates
     * the weighted average buy price.
     * <p>
     *     Formula: {@code newAvg = (oldAvg * oldQty + newPrice * newQty) / totalQty}
     * </p>
     *
     * @param newQuantity   the number of additional shares purchased
     * @param newPrice      the price per share paid for the new shares, in DKK
     */
    public void increaseQuantity(int newQuantity, double newPrice) {
        double totalCost = (this.averageBuyPrice * this.quantity) + (newPrice * newQuantity);
        this.quantity += newQuantity;
        this.averageBuyPrice = totalCost / this.quantity;
    }

    /**
     * Decreases the position size by the given number of shares.
     * <p>
     *     If the quantity to remove is greater than or equal to the current quantity,
     *     the position is reduced to zero rather than going negative.
     * </p>
     *
     * @param qty the number of shares to remove
     */
    public void decreaseQuantity(int qty) {
        if(this.quantity - qty <= 0){
            this.quantity = 0;
        }else{
            this.quantity -= qty;
        }
    }

    public double getCurrentValue() {
        return asset.getPrice() * quantity;
    }

    public double getCostBasis() {
        return averageBuyPrice * quantity;
    }


    public double getUnrealizedGain() {
        return getCurrentValue() - getCostBasis();
    }

    /**
     * Returns the percentage return of this position relative to its average buy price,
     * used for ranking positions by performance.
     * <p>
     *     Calculated as: {@code ((currentPrice - averageBuyPrice) / averageBuyPrice) * 100}
     * </p>
     *
     * @return percentage return as a {@code double}
     */
    @Override
    public double getRankValue() {
        return ((asset.getPrice() - averageBuyPrice) / averageBuyPrice) * 100;
    }

    /**
     * Serializes this position to a semicolon-delimited CSV line.
     * <p>
     *     Format: {@code ticker;quantity;averageBuyPrice}
     * </p>
     *
     * @return a CSV-formatted {@code String} representing this position
     */
    @Override
    public String toCSVLine() {
        return String.join(";",
                asset.getTicker(),
                String.valueOf(quantity),
                String.valueOf(averageBuyPrice));
    }

    /**
     * Returns a formatted string representation of this position for terminal display.
     * <p>
     *     Columns ticker, name, quantity, average buy price, current price, and unrealized gain/loss.
     *     The gain/loss is color-coded - green for gains, red for losses.
     * </p>
     *
     * @return a colored, tabular {@code String} suitable for terminal output
     */
    @Override
    public String toString() {
        String name = (asset instanceof Asset a) ? a.getName() : asset.getTicker();
        double gain = getUnrealizedGain();
        String gainColored = gain >= 0
                ? Colors.ANSI_GREEN + String.format("%+12.2f", gain) + Colors.RESET
                : Colors.ANSI_RED   + String.format("%12.2f",  gain) + Colors.RESET;

        return String.format("%-10s %-29s %8d %16.2f %16.2f %s",
                asset.getTicker(), name, quantity,
                averageBuyPrice, asset.getPrice(), gainColored);
    }

    /**
     * Compares this position to another by percentage return, in descending order.
     * <p>
     *     Used when sorting a portfolio so that the best-performing positions appear first.
     * <p>
     *
     * @param other the position to compare against
     * @return a negative integer if this position has a higher return than {@code other},
     *         zero if equal, or a positive integer if this position has a lower return
     */
    @Override
    public int compareTo(Position other) {
        return Double.compare(other.getRankValue(), this.getRankValue()); // descending — best return first
    }
}