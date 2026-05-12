package util.printing;

import util.constants.Colors;

import java.awt.*;

public final class ConsolePrinter {

    private ConsolePrinter() {
    }

    //Print color formatting
    public static void printError(String message) {
        System.out.println(Colors.ERROR + "\nPlease note: " + message + Colors.RESET);
    }

    public static void printConfirmation(String message) {
        System.out.println(Colors.CONFIRMATION + message + Colors.RESET);
    }

    public static void printMenuHeader(String message) {
        System.out.println(Colors.MENUHEADER + message + Colors.RESET);
    }

    public static void printMenuOption(String message) {
        System.out.println(Colors.MENUOPTION + message + Colors.RESET);
    }

    public static void printMenuTitle(String message) {
        System.out.println(Colors.SEPARATOR + message + Colors.RESET);
    }

    public static void printSeparator() {
        System.out.println(Colors.SEPARATOR + "─".repeat(100) + Colors.RESET);
    }



}