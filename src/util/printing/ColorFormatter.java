package util.printing;

import util.AppConstants;
import util.constants.Colors;

public class ColorFormatter {

    public static String conditionalAmountColor(double value) {
        String color;
        if (value > 0) color = Colors.ANSI_GREEN;
        else if (value < 0) color = Colors.ANSI_RED;
        else color = Colors.ANSI_YELLOW;

        return color + value + " " + AppConstants.BASE_CURRENCY + Colors.RESET;
    }
}
