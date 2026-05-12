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
        ConsolePrinter.printMenuOption("Welcome " + user.getFullName() + ", you current cash balance is " + (user.getCashBalance()) + " " + AppConstants.BASE_CURRENCY);
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
    // TODO: prompt for ticker and quantity
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
            double balanceAfterPurchase = (user.getCashBalance()) - totalCost;

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

            // --- delegate all logic to PortfolioService ---
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

    private void sellStock() {
        while (true){

            //Shows the user's portfolio so they know what they can sell
            viewPortfolio();

            Position position = null;
            String ticker = "";


            // TODO: prompt for ticker and quantity
            //Keep asking until the user enters a ticker they actually own
            while (position == null){
                ConsolePrinter.printMenuOption("Enter ticker of the stock you would like to sell: ");
                ticker = scanner.nextLine().trim().toUpperCase();

                //Look up the ticker in the portfolio, not the market
                position = user.getPortfolio().findByTicker(ticker);

                if (position == null) {
                    ConsolePrinter.printError("You do not own that stock. Please try again.");
                }
            }

            //Ask how many shares to sell
            ConsolePrinter.printMenuOption("Enter quantity");
            int quantity = Integer.parseInt(scanner.nextLine().trim());

            //Validate quantity doesn't exceed what they own
            if(quantity > position.getQuantity()){
                ConsolePrinter.printMenuOption("You only own " + position.getQuantity() + " shares of " + ticker + ". Starting over..." );
                continue;
            }

            //Calculate summary figures
            double unitPrice = position.getAsset().getPrice();
            double totalProceeds = unitPrice * quantity;
            double projectedCash = (user.getCashBalance()) + totalProceeds;

            //Display order summary - no changes happen yet
            ConsolePrinter.printMenuHeader("\n––– Order Summary –––");
            ConsolePrinter.printMenuOption("Ticker:           " + ticker.toUpperCase());
            ConsolePrinter.printMenuOption("Quantity:         " + quantity + " shares");
            ConsolePrinter.printMenuOption("Unit Price:       " + unitPrice + " DKK");
            ConsolePrinter.printMenuOption("Total Proceeds:   " + totalProceeds + " DKK");
            ConsolePrinter.printMenuOption("Cash After Sale   " + projectedCash + " DKK");

            ConsolePrinter.printMenuHeader("\nConfirm sale? (Yes/No)");
            String confirm = scanner.nextLine().trim().toLowerCase();

            // TODO: call portfolioService.sell() and handle any exceptions
            if(confirm.equals("yes")) {
                //Execute the sell - credits cash, reduces/removes position, returns Transaction
                Transaction transaction = portfolioService.sell(user, ticker, quantity);

                //Write transaction to CSV
                CSVWriter.append(AppConstants.TRANSACTIONS_FILE, transaction);

                ConsolePrinter.printConfirmation("Sale Complete!");

                //Ask if they want to sell another stock
                ConsolePrinter.printMenuOption("Sell another stock (Yes/No)");
                String another = scanner.nextLine().trim().toLowerCase();

                //If not, exit the loop and return to the menu
                if (!another.equals("y")) {
                    break;
                }
            } else {
                //Canceled - nothing written, loop restarts
                ConsolePrinter.printMenuOption("Sale canceled. Starting over...");
                sellStock();
            }
        }
        show();
    }

        private void viewMarket () {
            marketService.viewMarket();
            show();
        }
    }