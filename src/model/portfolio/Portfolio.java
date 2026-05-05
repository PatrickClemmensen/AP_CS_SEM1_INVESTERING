package model.portfolio;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    // TODO: declare a list to hold positions

    public void addPosition(Position position) {
        // TODO: add a position to the list
    }

    public List<Position> getPositions() {
        // TODO: return all positions
        return null;
    }

    public Position findByTicker(String ticker) {
        // TODO: find and return a position by ticker symbol, or null if not found
        // Hint: use a stream with a filter
        return null;
    }

    public double getTotalValue() {
        // TODO: sum the current value of all positions
        return 0;
    }

    public double getTotalGain() {
        // TODO: sum the unrealized gain across all positions
        return 0;
    }
}