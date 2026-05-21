package model.portfolio;

import interfaces.CSVSerializable;
import interfaces.Rankable;
import interfaces.Tradeable;
import util.constants.Colors;

/**
 * Represents a single position held in a member's portfolio.
 * <p>
 *     A position tracks how many units of a given {@link Tradeable} asset the member holds,
 *     at what average purchase price, and calculates current value and unrealized gain/loss.
 *     Works for both stocks and bonds since it holds a {@link Tradeable} reference.
 * </p>
 */
public class Position implements Rankable, CSVSerializable, Comparable<Position> {
    private final Tradeable asset;
    private int quantity;
    private double averageBuyPrice;

    /**
     * Constructs a new {@code Position} for the given asset.
     *
     * @param asset           the tradeable asset this position represents
     * @param quantity        the number of units initially purchased
     * @param averageBuyPrice the price per unit at the time of purchase
     */
    public Position(Tradeable asset, int quantity, double averageBuyPrice) {
        this.asset          = asset;
        this.quantity       = quantity;
        this.averageBuyPrice = averageBuyPrice;
    }

    public Tradeable getAsset()        { return asset; }
    public int getQuantity()           { return quantity; }
    public double getAverageBuyPrice() { return averageBuyPrice; }

    /**
     * Increases the position size and recalculates the weighted average buy price.
     * <p>
     *     Formula: {@code newAvg = (oldAvg * oldQty + newPrice * newQty) / totalQty}
     * </p>
     *
     * @param newQuantity the number of additional units purchased
     * @param newPrice    the price per unit for the new purchase
     */
    public void increaseQuantity(int newQuantity, double newPrice) {
        double totalCost     = (this.averageBuyPrice * this.quantity) + (newPrice * newQuantity);
        this.quantity       += newQuantity;
        this.averageBuyPrice = totalCost / this.quantity;
    }

    /**
     * Decreases the position size by the given number of units.
     *
     * @param qty the number of units to remove
     */
    public void decreaseQuantity(int qty) {
        this.quantity = Math.max(0, this.quantity - qty);
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
     * Returns the percentage return of this position relative to its average buy price.
     * <p>
     *     Used by {@link Comparable#compareTo} and {@link util.comparator.ByPercentReturn}.
     * </p>
     *
     * @return percentage return as a {@code double}
     */
    @Override
    public double getRankValue() {
        return ((asset.getPrice() - averageBuyPrice) / averageBuyPrice) * 100;
    }

    /**
     * Compares this position to another by percentage return, descending.
     *
     * @param other the position to compare against
     * @return negative if this position has a higher return, positive if lower
     */
    @Override
    public int compareTo(Position other) {
        return Double.compare(other.getRankValue(), this.getRankValue());
    }

    @Override
    public String toCSVLine() {
        return String.join(";",
                asset.getTicker(),
                String.valueOf(quantity),
                String.valueOf(averageBuyPrice));
    }

    @Override
    public String toString() {
        double gain = getUnrealizedGain();
        String gainColored = gain >= 0
                ? Colors.ANSI_GREEN + String.format("%+12.2f", gain) + Colors.RESET
                : Colors.ANSI_RED   + String.format("%12.2f",  gain) + Colors.RESET;

        return String.format("%-10s %-29s %8d %16.2f %16.2f %s",
                asset.getTicker(), asset.getName(), quantity,
                averageBuyPrice, asset.getPrice(), gainColored);
    }
}