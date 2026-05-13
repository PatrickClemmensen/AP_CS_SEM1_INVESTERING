package util.validation;

import util.printing.ConsolePrinter;

import java.util.Scanner;

public class MenuChoiceValidator {

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
