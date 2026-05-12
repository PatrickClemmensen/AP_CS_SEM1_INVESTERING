package ui;

import model.asset.Stock;
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

    Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for MemberMenu object.
     * Creates a MemberMenu for the given user, backed by the provided services.
     *
     * @param user             the logged in user
     * @param marketService    - live data from the market
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
        show();
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                MemberOption option = MemberOption.fromChoice(input);
                if (option == MemberOption.EXIT) {
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
     *
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
        ConsolePrinter.printMenuHeader("\n=========================================== MY PORTFOLIO ===========================================\n");
        System.out.printf("%-10s %-29s %8s %16s %16s %16s%n", "TICKER", "NAME", "QTY", "AVG BUY", "VALUE", "POT. GAIN");
        ConsolePrinter.printSeparator();

        if (user.getPortfolio().getPositions().isEmpty()) {
            ConsolePrinter.printError("Your portfolio is empty.");
        } else {
            for (Position position : user.getPortfolio().getPositions()) {
                ConsolePrinter.printMenuOption(position.toString());
            }
            ConsolePrinter.printSeparator();
            double totalGain = user.getPortfolio().getTotalGain();
            String totalGainColored = totalGain >= 0
                    ? Colors.ANSI_GREEN + String.format("%+12.2f DKK", totalGain) + Colors.RESET
                    : Colors.ANSI_RED + String.format("%12.2f DKK", totalGain) + Colors.RESET;

            System.out.println(Colors.MENUOPTION + String.format("%-12s %12.2f DKK     %-10s %s",
                    "Total Value:", user.getPortfolio().getTotalValue(),
                    "Total Gain:", totalGainColored) + Colors.RESET);
        }

        show();
    }
    // TODO: prompt for ticker and quantity
    private void buyStock() {
        while(true){
        ConsolePrinter.printConfirmation("Buy stock");
        marketService.viewMarket();

            Stock stock = null; //Creates a stock-vale before the loop starts

            while (stock == null) { //As long as stock is null, the user will be asked again
                ConsolePrinter.printMenuOption("Please enter ticker:    |   Or press 0 to cancel");
                String ticker = scanner.nextLine().trim().toUpperCase();

                if (ticker.equals("0")) {
                    ConsolePrinter.printConfirmation("Purchase cancelled");
                    return;
                }
                    stock = marketService.findByTicker(ticker); //importerer Stock. Kalder metoden findByTicker.

                    if (stock == null) {
                        ConsolePrinter.printError("No stock found with ticker: " + ticker); //Hvis der ikke findes en stock for den ticker vil den give fejl
                        ConsolePrinter.printMenuOption("Please try again.");
                    }
                }

                ConsolePrinter.printConfirmation("Selected stock: " + stock.getTicker());

                    ConsolePrinter.printMenuOption("Enter quantity for " + stock.getTicker() + ":");

                    int quantity = Integer.parseInt(scanner.nextLine().trim()); //Changes String input to int

                    double pricePerStock = stock.getPrice();//What one stock costs
                    double totalCost = pricePerStock * quantity;//All stocks together
                    double balanceAfterPurchase = (user.getCashBalance() - user.getPortfolio().getTotalValue()) - totalCost;//Users potential cash balance after the purchase. The purchase isn't done here.

                    //Trade summary:
                    ConsolePrinter.printMenuHeader("TRADE SUMMARY");
                    ConsolePrinter.printSeparator();
                    System.out.println("Ticker: " + stock.getTicker());
                    System.out.println("Quantity: " + quantity);
                    System.out.printf("Price per stock: %.2f DKK%n", pricePerStock);
                    System.out.printf("Total cost: %.2f DKK%n", totalCost);
                    System.out.printf("Your cash balance after the purchase: %.2f DKK%n", balanceAfterPurchase);
                    ConsolePrinter.printSeparator();
                    ConsolePrinter.printMenuOption("Confirm purchase? Type y or n:");
                    String confirmation = scanner.nextLine().trim().toLowerCase(); //reads user input

        if (confirmation.equals("y")) {
            //portfolioService handles logic
            portfolioService.buy(user, stock.getTicker(), quantity); //purchase is done. witdraws money, updates position, writes transaction to transactions.csv
            ConsolePrinter.printConfirmation("Purchase completed!");

            ConsolePrinter.printMenuOption("Buy another stock? (y or n)");
            String another = scanner.nextLine().trim().toLowerCase();

            if (!another.equals("y")) {
                start();
            }
            } else {
                ConsolePrinter.printError("Purchase cancelled. Starting over...");
            }
        }
    }
    // TODO: call portfolioService.buy() and handle any exceptions

        private void sellStock () {
            ConsolePrinter.printConfirmation("Placeholder for sellStock()");
            // TODO: prompt for ticker and quantity
            // TODO: call portfolioService.sell() and handle any exceptions
        }

        private void viewMarket () {
            marketService.viewMarket();
            show();
        }
    }
