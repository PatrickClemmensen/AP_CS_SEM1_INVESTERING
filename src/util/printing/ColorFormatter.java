package util.printing;

import util.AppConstants;
import util.constants.Colors;

/**
 * Utility class for applying ANSI color formatting to numeric values
 * displayed in the console.
 * <p>
 *     this class is not meant to be instantiated - all methods are static
 * </p>
 */
public class ColorFormatter {

    /**
     * Returns the given monetary value as an ASNI-colored string:
     * green for positive, red for negative, yellow for zero.
     * Appends the base currency label (e.g. {@code DKK}) and resets the color.
     *
     * @param value the monetary amount to format
     * @return a color-coded string with the value and base currency
     */
    public static String conditionalAmountColor(double value) {
        String color;
        if (value > 0) color = Colors.ANSI_GREEN;
        else if (value < 0) color = Colors.ANSI_RED;
        else color = Colors.ANSI_YELLOW;

        return color + value + " " + AppConstants.BASE_CURRENCY + Colors.RESET;
    }
}
