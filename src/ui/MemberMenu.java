package ui;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import util.AppConstants;
import util.printing.ConsolePrinter;
import java.util.Scanner;
import static ui.MemberOption.OPTION_1;

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
        private final Scanner scanner = new Scanner(System.in);

        ConsolePrinter.printMenuHeader("Welcome " + user.getFullName() + ", you current cash balance is " + user.getCashBalance() + " " + AppConstants.BASE_CURRENCY);

        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                MemberOption option = MemberOption.fromChoice(input);
                if (option == MemberOption.EXIT) break;
                handleChoice(option);
            } catch (IllegalArgumentException e) {
                ConsolePrinter.printError("Invalid choice, try again.");
            }
        }

        private void show() {
            ConsolePrinter.printSeparator();
            ConsolePrinter.printMenuHeader("=== MEMBER MENU ===");
            ConsolePrinter.printSeparator();
            for (MemberOption option : MemberOption.values()) {
                ConsolePrinter.printMenuOption(option.getValue() + ". " + option.getLabel());
            }
        }

        private void handleChoice(MemberOption option) {
            switch (option) {
                case OPTION_1 -> ConsolePrinter.printConfirmation("Going to option 1");
            }
        }
    }
        // TODO: read user input and route to the appropriate method
    }

    private void viewPortfolio() {
        // TODO: print all positions in the user's portfolio
        // TODO: show total value and total unrealized gain
    }

    private void buyStock() {
        // TODO: prompt for ticker and quantity
        // TODO: call portfolioService.buy() and handle any exceptions
    }

    private void sellStock() {
        // TODO: prompt for ticker and quantity
        // TODO: call portfolioService.sell() and handle any exceptions
    }

    private void viewMarket() {
        // TODO: print all available stocks from marketService.getAllStocks()
    }
}