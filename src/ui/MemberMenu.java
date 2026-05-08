package ui;

import model.portfolio.Portfolio;
import model.portfolio.Position;
import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import util.AppConstants;
import util.constants.Colors;
import util.printing.ConsolePrinter;
import java.util.Scanner;

/**
 * Menu flow for Club Members.
 *
 * A Club Member can do the following:
 *       <ul>
 *           <li>View Portfolio</li>
 *           <li>Buy Stock</li>
 *           <li>Sell Stock</li>
 *           <li>View Market</li>
 *       </ul>
 *
 */
public class MemberMenu {
    private User user;
    private StockMarketService marketService;
    private PortfolioService portfolioService;

    /**
     * Constructor for MemberMenu object.
     * Creates a MemberMenu for the given user, backed by the provided services.
     * @param user the logged in user
     * @param marketService - live data from the market
     * @param portfolioService - live data from the user's portfolio
     */
    public MemberMenu(User user, StockMarketService marketService, PortfolioService portfolioService) {
        this.user = user;
        this.marketService = marketService;
        this.portfolioService = portfolioService;
        portfolioService.loadPortfolio(user);
    }

    /**
     * Initiation of the menu process - displays the menu and loops until the user chooses to exit..
     * Also contains the logic for exiting the menu.
     */
    public void start() {
        Scanner scanner = new Scanner(System.in);
        show();
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                MemberOption option = MemberOption.fromChoice(input);
                if (option == MemberOption.EXIT) {
                    ConsolePrinter.printConfirmation("Logout succesful...");
                    break;}
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
        ConsolePrinter.printSeparator();
        ConsolePrinter.printMenuHeader("Welcome " + user.getFullName() + ", you current cash balance is " + (user.getCashBalance() - user.getPortfolio().getTotalValue()) + " " + AppConstants.BASE_CURRENCY);
        ConsolePrinter.printSeparator();
        for (MemberOption option : MemberOption.values()) {
            ConsolePrinter.printMenuOption(option.getValue() + ". " + option.getLabel());
        }
        ConsolePrinter.printSeparator();
    }

    /**
     * Directs the user to a new submenu based on the option they choose.
     * @param option choice made by the logged-in user
     */
    private void handleChoice(MemberOption option) {
        switch (option) {
            case OPTION_1 -> viewPortfolio();
            case OPTION_2 -> buyStock();
            case OPTION_3 -> sellStock();
            case OPTION_4 -> viewMarket();
        }
    }

    private void viewPortfolio() {
        System.out.println(Colors.MENUHEADER + "\n============================== MY PORTFOLIO ===============================\n" + Colors.RESET);
        System.out.printf("%-10s %-25s %8s %12s %12s %12s%n", "TICKER", "NAME", "QTY", "AVG BUY", "VALUE", "POT. GAIN");
        System.out.println("-".repeat(75));

        if (user.getPortfolio().getPositions().isEmpty()) {
            System.out.println("Your portfolio is empty.");
        } else {
            for (Position position : user.getPortfolio().getPositions()) {
                System.out.println(Colors.MENUOPTION + position + Colors.RESET);
            }
            System.out.println("-".repeat(75));
            System.out.println(Colors.MENUOPTION + String.format("%-12s %12.2f DKK     %-10s %12.2f DKK",
                    "Total Value:", user.getPortfolio().getTotalValue(),
                    "Total Gain:", user.getPortfolio().getTotalGain()) + Colors.RESET);
        }

        show();
    }

    private void buyStock() {
        ConsolePrinter.printConfirmation("Placeholder for buyStock()");
        // TODO: prompt for ticker and quantity
        // TODO: call portfolioService.buy() and handle any exceptions
    }

    private void sellStock() {
        ConsolePrinter.printConfirmation("Placeholder for sellStock()");
        // TODO: prompt for ticker and quantity
        // TODO: call portfolioService.sell() and handle any exceptions
    }

    private void viewMarket() {
        marketService.viewMarket();
        show();
    }
}