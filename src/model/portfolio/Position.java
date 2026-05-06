package model.portfolio;

import interfaces.CSVSerializeable;
import interfaces.Rankable;
import model.asset.Asset;

public class Position implements Rankable, CSVSerializeable {
    // TODO: declare fields (asset, quantity, averageBuyPrice)
    // Note: averageBuyPrice should NOT be final — it updates on each additional purchase

    public Position(Asset asset, int quantity, double averageBuyPrice) {
        // TODO: initialize fields
    }

    // TODO: add getters for asset, quantity, averageBuyPrice

    public void addQuantity(int quantity) {
        // TODO: add the given quantity to the current quantity
    }

    public void setAverageBuyPrice(double averageBuyPrice) {
        // TODO: update the averageBuyPrice
        // Note: this is called by PortfolioService after recalculating the weighted average
    }

    public double getCurrentValue() {
        // TODO: calculate current market value of this position
        // Hint: asset.getPrice() * quantity
        return 0;
    }

    public double getCostBasis() {
        // TODO: calculate what was originally paid
        // Hint: averageBuyPrice * quantity
        return 0;
    }

    public double getUnrealizedGain() {
        // TODO: calculate profit/loss vs cost basis
        // Hint: getCurrentValue() - getCostBasis()
        return 0;
    }

    @Override
    public double getRankValue() {
        // TODO: return percent return for use in ByPercentReturn comparator
        // Hint: ((currentPrice - averageBuyPrice) / averageBuyPrice) * 100
        return 0;
    }

    @Override
    public String toCSVLine() {
        // TODO: return semicolon-delimited string
        return null;
    }

    @Override
    public String toString() {
        // TODO: return a readable summary of this position
        return null;
    }
}