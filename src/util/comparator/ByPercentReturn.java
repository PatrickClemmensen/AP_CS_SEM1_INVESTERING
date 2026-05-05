package util.comparator;

import interfaces.Rankable;
import java.util.Comparator;

public class ByPercentReturn implements Comparator<Rankable> {

    @Override
    public int compare(Rankable a, Rankable b) {
        // TODO: compare by getRankValue() in descending order (best return first)
        // Hint: Double.compare(b.getRankValue(), a.getRankValue())
        return 0;
    }
}