package model.portfolio;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    // TODO: declare a list to hold positions
    List<Position> portfolio = new ArrayList<>();

    public void addPosition(Position position) {
        portfolio.add(position);
    }

    public void removePosition(Position position) {
        portfolio.remove(position);
    }

    public List<Position> getPositions() {
        return portfolio;
    }

    public Position findByTicker(String ticker) {
        // TODO: find and return a position by ticker symbol, or null if not found
        // Hint: use a stream with a filter

        return portfolio.stream()
                .filter(p -> p.getAsset().getTicker().equals(ticker))
                .findFirst()
                .orElse(null);
    }

    public double getTotalValue() {
        return portfolio.stream()
                .mapToDouble(Position::getCurrentValue)
                .sum();
    }

    public double getTotalGain() {
        return portfolio.stream()
                .mapToDouble(Position::getUnrealizedGain)
                .sum();
    }
}