package util.constants;

/**
 * Utility class containing ANSI color codes for console output formatting.
 * <P>
 *     Use {@link #RESET} after every colored output to prevent
 *     colors from contintuing into text thats not supposed to have said
 *     same color
 * </P>
 */
public class Colors {

    public static final String RESET = "\u001B[0m";
    public static final String ERROR = "\u001B[31m";
    public static final String CONFIRMATION = "\u001B[32m";
    public static final String MENUHEADER = "\u001B[34m";
    public static final String MENUOPTION = "\u001B[36m";

}
