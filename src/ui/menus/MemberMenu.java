package ui.menus;

import model.asset.Stock;
import model.portfolio.Position;
import model.portfolio.User;
import model.transaction.Transaction;
import service.PortfolioService;
import service.StockMarketService;
import ui.enums.MemberOption;
import util.AppConstants;
import util.constants.Colors;
import util.csv.CSVWriter;
import util.printing.ConsolePrinter;

import java.util.Scanner;

/**
 * Menu flow for Club Members.
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
    private Scanner scanner = new Scanner(System.in);

    /**
     * Constructor for MemberMenu object.
     * Creates a MemberMenu for the given user, backed by the provided services.
     * @param user the logged-in user
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
     * Initiation of the menu process - displays the menu and loops until the user chooses to exit.
     * Also contains the logic for exiting the menu.
     */
    public void start() {
        show();
        while (true) {
            try {
                int input = Integer.parseInt(scanner.nextLine().trim());
                MemberOption option = MemberOption.fromChoice(input);
                if (option == MemberOption.EXIT) {
                    ConsolePrinter.printConfirmation("Logout successful...");
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
        ConsolePrinter.printMenuTitle("─────────────────────────────────────────── Club Member ───────────────────────────────────────────");
        ConsolePrinter.printMenuOption("Welcome " + user.getFullName()
                + ", your current cash balance is " + user.getCashBalance() + " " + AppConstants.BASE_CURRENCY
                + ", total portfolio value: " + (user.getCashBalance() + user.getPortfolio().getTotalValue()));
        ConsolePrinter.printSeparator();
        for (MemberOption option : MemberOption.values()) {
            ConsolePrinter.printMenuOption(option.getValue() + ". " + option.getLabel());
        }
        ConsolePrinter.printSeparator();
    }

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
                    : Colors.ANSI_RED   + String.format("%12.2f DKK",  totalGain) + Colors.RESET;

            System.out.println(Colors.MENUOPTION + String.format("%-12s %12.2f DKK     %-10s %s",
                    "Total Value:", user.getPortfolio().getTotalValue(),
                    "Total Gain:", totalGainColored) + Colors.RESET);
        }

        show();
    }
    /**
     * Handles the flow for when a user wants to buy a stock.
     * <p>
     * The method shows the market, asks the user to enter a ticker and quantity for the wanted stock, shows a trade summary, and asks for confirmation before completing the purchase.
     * </p>
     *      * The user can cancel the purchase by pressing 0 when asked for a ticker.
     * If the user confirms the purchase, the method calls {@link PortfolioService#buy(User, String, int)} to perform the actual buying logic
     * The {@link Transaction} is then saved to the transactions csv file.
     * </p>
     * Invalid tickers, invalid quantities, insufficient funds or other buy errors are handled by displaying an error message.
     * The user is also given the option to buy another stock.
     * When the flow is done the user is sent back to see the club member menu.
     */
    private void buyStock() {
        while (true) {
            marketService.viewMarket();

            // --- ticker input ---
            ConsolePrinter.printMenuOption("Please enter ticker:    |   Or press 0 to cancel");
            String ticker = scanner.nextLine().trim().toUpperCase();

            if (ticker.equals("0")) {
                ConsolePrinter.printConfirmation("Purchase cancelled");
                start();
                return;
            }

            Stock stock = marketService.findByTicker(ticker);
            if (stock == null) {
                ConsolePrinter.printError("No stock found with ticker: " + ticker + ". Please try again.");
                continue;
            }

            // --- quantity input ---
            ConsolePrinter.printMenuOption("Enter quantity for " + stock.getTicker() + ":");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid quantity. Please try again.");
                continue;
            }

            // --- trade summary ---
            double pricePerStock = stock.getPrice();
            double totalCost = pricePerStock * quantity;
            double balanceAfterPurchase = user.getCashBalance() - totalCost;

            ConsolePrinter.printMenuHeader("TRADE SUMMARY");
            ConsolePrinter.printSeparator();
            System.out.println("Ticker: " + stock.getTicker());
            System.out.println("Quantity: " + quantity);
            System.out.printf("Price per stock: %.2f DKK%n", pricePerStock);
            System.out.printf("Total cost: %.2f DKK%n", totalCost);
            System.out.printf("Balance after purchase: %.2f DKK%n", balanceAfterPurchase);
            ConsolePrinter.printSeparator();

            // --- confirmation ---
            ConsolePrinter.printMenuOption("Confirm purchase? (y / n):");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("y")) {
                ConsolePrinter.printError("Purchase cancelled. Starting over...");
                continue;
            }

            // --- delegate to PortfolioService ---
            try {
                Transaction transaction = portfolioService.buy(user, ticker, quantity);
                CSVWriter.append(AppConstants.TRANSACTIONS_FILE, transaction);
                ConsolePrinter.printConfirmation("Purchase completed!");
            } catch (Exception e) {
                ConsolePrinter.printError(e.getMessage());
                continue;
            }

            // --- buy another? ---
            ConsolePrinter.printMenuOption("Buy another stock? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                start();
                return;
            }
        }
    }
    // TODO: call portfolioService.buy() and handle any exceptions

    /**
     * Handles the flow for when a user wants to buy a stock.
     * <p>
     * The method show the user's portfolio, asks the user to enter a ticker and quantity for the stock they want to sell,
     * shows a trade summary, and asks for confirmation before completing the sale.
     * </p>
     * <p>
     * The user can cansel the sale by pressing 0 when asked for a ticker.
     * If the user confirms the sale, the method calls {@link PortfolioService#sell(User, String, int)} to perform the actual selling logic.
     * The {@link Transaction} is then saved to the transactions csv file.
     * </p>
     * <p>
     * Invalid tickers, invalid quantities, insufficient stock quantity, or other sale errors are handled by displaying an error message.
     * The user is also given the option to sell another stock.
     * When the flow is done, the user is sent back to the club member menu.
     * </p>
     */
    private void sellStock() {
        while (true){

            //Shows the user's portfolio so they know what they can sell
            viewPortfolio();

            if (user.getPortfolio().getPositions().isEmpty()) {
                return;
            }

            // --- ticker input ---
            Position position = null;
            String ticker = "";

            while (position == null) {
                ConsolePrinter.printMenuOption("Enter ticker of the stock you would like to sell:   |   Or press 0 to cancel");
                ticker = scanner.nextLine().trim().toUpperCase();

                if (ticker.equals("0")) {
                    ConsolePrinter.printConfirmation("Sale cancelled");
                    show();
                    return;
                }

                position = user.getPortfolio().findByTicker(ticker);
                if (position == null) {
                    ConsolePrinter.printError("You do not own that stock. Please try again.");
                }
            }

            // --- quantity input ---
            ConsolePrinter.printMenuOption("Enter quantity:");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid quantity. Please try again.");
                continue;
            }

            if (quantity > position.getQuantity()) {
                ConsolePrinter.printError("You only own " + position.getQuantity() + " shares of " + ticker + ". Starting over...");
                continue;
            }

            // --- order summary ---
            double unitPrice = position.getAsset().getPrice();
            double totalProceeds = unitPrice * quantity;
            double projectedCash = user.getCashBalance() + totalProceeds;

            ConsolePrinter.printMenuHeader("ORDER SUMMARY");
            ConsolePrinter.printSeparator();
            ConsolePrinter.printMenuOption("Ticker:          " + ticker);
            ConsolePrinter.printMenuOption("Quantity:        " + quantity + " shares");
            ConsolePrinter.printMenuOption("Unit Price:      " + unitPrice + " DKK");
            ConsolePrinter.printMenuOption("Total Proceeds:  " + totalProceeds + " DKK");
            ConsolePrinter.printMenuOption("Cash After Sale: " + projectedCash + " DKK");
            ConsolePrinter.printSeparator();

            // --- confirmation ---
            ConsolePrinter.printMenuOption("Confirm sale? (y / n):");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (!confirm.equals("y")) {
                ConsolePrinter.printError("Sale cancelled. Starting over...");
                continue;
            }

            // --- delegate to PortfolioService ---
            try {
                portfolioService.sell(user, ticker, quantity);
                ConsolePrinter.printConfirmation("Sale complete!");
            } catch (Exception e) {
                ConsolePrinter.printError(e.getMessage());
                continue;
            }

            // --- sell another? ---
            ConsolePrinter.printMenuOption("Sell another stock? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                show();
                return;
            }
        }
    }

    private void viewMarket() {
        marketService.viewMarket();
        show();
    }
}