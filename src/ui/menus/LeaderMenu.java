package ui.menus;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import ui.enums.LeaderOption;
import util.printing.ConsolePrinter;

import java.util.Scanner;

public class LeaderMenu {
    private StockMarketService marketService;
    private PortfolioService portfolioService;
    private Scanner scanner = new Scanner(System.in);

    public LeaderMenu(StockMarketService marketService, PortfolioService portfolioService) {
        this.marketService = marketService;
        this.portfolioService = portfolioService;
    }

    /**
     * Initiation of the menu process - displays the menu and loops until the user chooses to exit..
     * Also contains the logic for exiting the menu.
     */
    public void start() {
        show();
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                LeaderOption option = LeaderOption.fromChoice(input);
                if (option == LeaderOption.EXIT) {
                    ConsolePrinter.printConfirmation("Logout succesful...");
                    break;
                }
                handleChoice(option);
            } catch (IllegalArgumentException e) {
                ConsolePrinter.printError("Invalid choice, try again.");
            }
        }
    }

    /**
     * Prints the menu itself with the logged-in user's name and their current cash balance.
     */
    private void show() {
        System.out.println();
        ConsolePrinter.printMenuTitle("─────────────────────────────────────────── Club Leader ───────────────────────────────────────────");
        ConsolePrinter.printMenuOption("As the Club Leader, you have exclusive rights to perform the following actions");
        ConsolePrinter.printSeparator();
        for (LeaderOption option : LeaderOption.values()) {
            ConsolePrinter.printMenuOption(option.getValue() + ". " + option.getLabel());
        }
        ConsolePrinter.printSeparator();
    }

    /**
     * Directs the user to a new submenu based on the option they choose.
     * @param option choice made by the logged-in user
     */
    private void handleChoice(LeaderOption option) {
        switch (option) {
            case OPTION_1 -> System.out.println("View all members");

        }
    }


}
