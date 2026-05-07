package ui;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import util.AppConstants;
import util.printing.ConsolePrinter;
import java.util.Scanner;

public class MemberMenu {
    // TODO: declare references to user, marketService, portfolioService - DONE
    private User user;
    private StockMarketService marketService;
    private PortfolioService portfolioService;

    public MemberMenu(User user, StockMarketService marketService, PortfolioService portfolioService) {
        // TODO: initialize fields - DONE
        this.user = user;
        this.marketService = marketService;
        this.portfolioService = portfolioService;
    }

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

    public void show() {
        ConsolePrinter.printSeparator();
        ConsolePrinter.printMenuHeader("Welcome " + user.getFullName() + ", you current cash balance is " + user.getCashBalance() + " " + AppConstants.BASE_CURRENCY);
        ConsolePrinter.printSeparator();
        for (MemberOption option : MemberOption.values()) {
            ConsolePrinter.printMenuOption(option.getValue() + ". " + option.getLabel());
        }
        ConsolePrinter.printSeparator();
    }

    // TODO: read user input and route to the appropriate method - DONE
    private void handleChoice(MemberOption option) {
        switch (option) {
            case OPTION_1 -> viewPortfolio();
            case OPTION_2 -> buyStock();
            case OPTION_3 -> sellStock();
            case OPTION_4 -> viewMarket();
        }
    }

    private void viewPortfolio() {
        ConsolePrinter.printConfirmation("Placeholder for viewPortfolio()");
        // TODO: print all positions in the user's portfolio
        // TODO: show total value and total unrealized gain
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
        ConsolePrinter.printConfirmation("Placeholder for viewMarket()");
        // TODO: print all available stocks from marketService.getAllStocks()
    }
}