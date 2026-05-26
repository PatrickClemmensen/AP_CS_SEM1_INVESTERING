package util.validation;

import util.printing.ConsolePrinter;

import java.util.Scanner;

/**
 * Utility class for reading and validating numeric menu choices from the console.
 *
 * <p>This class is not meant to be instantiated — all methods are static.</p>
 */
public class MenuChoiceValidator {

    /**
     * Repeatedly prompts the user until a valid integer within [{@code min}, {@code max}] is entered.
     * Prints an error and retries if the input is out of range or not a number.
     *
     * @param scanner the {@link Scanner} to read input from
     * @param min     the minimum accepted value (inclusive)
     * @param max     the maximum accepted value (inclusive)
     * @param action  description of what entering {@code 0} will do (used in the error message)
     * @return a valid integer choice within the specified range
     */
    public static int readChoice(Scanner scanner, int min, int max, String action) {
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                if (input < min || input > max) {
                    ConsolePrinter.printError("Enter a number between '" + min + "' and '" + max + "'. If you enter '0' you will " + action + ".");
                    continue;
                }
                return input;
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid input. Please enter a valid number.");
            }
        }
    }
}
