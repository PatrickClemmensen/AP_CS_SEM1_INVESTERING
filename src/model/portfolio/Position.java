package model.portfolio;

import interfaces.CSVSerializable;
import interfaces.Rankable;
import model.asset.Asset;
import util.constants.Colors;

public class Position implements Rankable, CSVSerializable {
    // TODO: declare fields (asset, quantity, averageBuyPrice)
    // Note: averageBuyPrice should NOT be final — it updates on each additional purchase
    private final Asset asset;
    private int quantity;
    private double averageBuyPrice;


    public Position(Asset asset, int quantity, double averageBuyPrice) {
        // TODO: initialize fields
        this.asset = asset;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
    }

    // TODO: add getters for asset, quantity, averageBuyPrice

    public Asset getAsset() { return asset; }
    public int getQuantity() { return quantity; }
    public double getAverageBuyPrice() { return averageBuyPrice; }


    public void increaseQuantity(int newQuantity, double newPrice) {
        double totalCost = (this.averageBuyPrice * this.quantity) + (newPrice * newQuantity);
        this.quantity += newQuantity;
        this.averageBuyPrice = totalCost / this.quantity;
    }

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

    @Override
    public double getRankValue() {
        return ((asset.getPrice() - averageBuyPrice) / averageBuyPrice) * 100;
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