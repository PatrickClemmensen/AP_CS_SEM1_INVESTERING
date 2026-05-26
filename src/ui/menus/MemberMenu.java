package ui.menus;

import model.asset.Stock;
import model.asset.Bond;
import model.portfolio.Position;
import model.portfolio.User;
import model.transaction.Transaction;
import service.BondMarketService;
import service.PortfolioService;
import service.StockMarketService;
import ui.enums.MemberOption;
import util.AppConstants;
import util.constants.Colors;
import util.csv.CSVWriter;
import util.printing.ColorFormatter;
import util.printing.ConsolePrinter;
import util.validation.MenuChoiceValidator;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Handles the menu flow and user interactions for a logged-in club member.
 * A Club Member can do the following:
 *       <ul>
 *           <li>View Portfolio</li>
 *           <li>Buy Stock</li>
 *           <li>Sell Stock</li>
 *           <li>View Market</li>
 *       </ul>
 */
public class MemberMenu {
    private final User user;
    private final StockMarketService marketService;
    private final BondMarketService bondMarketService;
    private final PortfolioService portfolioService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a MemberMenu for the given user, backed by the provided services.
     *
     * @param user              the logged-in user
     * @param marketService     service for accessing stock market data
     * @param bondMarketService service for accessing bond market data
     * @param portfolioService  service for loading and managing the user's portfolio
     */
    public MemberMenu(User user, StockMarketService marketService,
                      BondMarketService bondMarketService, PortfolioService portfolioService) {
        this.user               = user;
        this.marketService      = marketService;
        this.bondMarketService  = bondMarketService;
        this.portfolioService   = portfolioService;
        portfolioService.loadPortfolio(user);
    }

    /**
     * Initiation of the menu process - displays the menu and loops until the user chooses to exit.
     * Breaks the loop and logs the user out when EXIT is chosen.
     */
    public void start() {
        show();
        while (true) {
            try {
                int input = MenuChoiceValidator.readChoice(scanner, 0, 6, "logout");
                MemberOption option = MemberOption.fromChoice(input);
                if (option == MemberOption.EXIT) {
                    ConsolePrinter.printConfirmation("Logout successful!");
                    show();
                    break;
                }
                handleChoice(option);
            } catch (IllegalArgumentException e) {
                ConsolePrinter.printError("Invalid choice, try again.");
            }
        }
    }

    /**
     * Prints the Club Member menu with the logged-in user's name, cash balance, and total combined value.
     */
    private void show() {
        System.out.println();
        ConsolePrinter.printMenuTitle("─────────────────────────────────────────── Club Member ───────────────────────────────────────────");
        ConsolePrinter.printMenuOption("Welcome " + user.getFullName()
                + ", your current cash balance is " + ColorFormatter.conditionalAmountColor(user.getCashBalance())
                + Colors.MENUOPTION + ", your total value is: " + ColorFormatter.conditionalAmountColor(user.getCashBalance() + user.getPortfolio().getTotalValue()));
        ConsolePrinter.printSeparator();
        for (MemberOption option : MemberOption.values()) {
            ConsolePrinter.printMenuOption(option.getValue() + ". " + option.getLabel());
        }
        ConsolePrinter.printSeparator();
    }

    /**
     * Directs the user to the appropriate method based on the chosen menu option.
     *
     * @param option the menu option chosen by the logged-in user
     */
    private void handleChoice(MemberOption option) {
        switch (option) {
            case OPTION_1 -> viewPortfolio();
            case OPTION_2 -> buyAsset();
            case OPTION_3 -> sellAsset();
            case OPTION_4 -> viewMarket();
            case OPTION_5 -> viewTransactions();
            case OPTION_6 -> searchStocks();
        }
    }

    /**
     * Displays the portfolio view and returns the user to the MemberMenu afterwards.
     */
    private void viewPortfolio(){
        printPortfolio();
        show();
    }

    /**
     * Displays the user's portfolio positions in a table, followed by a summary showing
     * total holdings value, total combined value (cash + holdings), total gain, and current cash balance.
     * Does not navigate back to the MemberMenu — used directly by {@link #viewPortfolio()} and {@link #sellStock()}.
     */
    private void printPortfolio() {
        System.out.println();
        ConsolePrinter.printMenuTitle("──────────────────────────────────────────── My Portfolio ─────────────────────────────────────────");

        if (user.getPortfolio().getPositions().isEmpty()) {
            ConsolePrinter.printError("Your portfolio is empty.");
            return;
        }

        // --- Stocks ---
        List<Position> stockPositions = user.getPortfolio().getPositions().stream()
                .filter(p -> p.getAsset() instanceof Stock)
                .toList();

        if (!stockPositions.isEmpty()) {
            ConsolePrinter.printMenuHeader("Stocks");
            System.out.printf("%-10s %-29s %8s %16s %16s %16s%n",
                    "TICKER", "NAME", "QTY", "AVG BUY", "PRICE", "POT. GAIN");
            ConsolePrinter.printSeparator();
            for (Position position : stockPositions) {
                ConsolePrinter.printMenuOption(position.toString());
            }
            System.out.println();
        }

        // --- Bonds ---
        List<Position> bondPositions = user.getPortfolio().getPositions().stream()
                .filter(p -> p.getAsset() instanceof Bond)
                .toList();

        if (!bondPositions.isEmpty()) {
            ConsolePrinter.printMenuHeader("Bonds");
            System.out.printf("%-10s %-20s %6s %14s %14s %9s %13s %12s%n",
                    "TICKER", "NAME", "QTY", "AVG BUY", "PRICE", "COUPON %", "MATURES", "POT. GAIN");
            ConsolePrinter.printSeparator();
            for (Position position : bondPositions) {
                Bond bond = (Bond) position.getAsset();
                double gain = position.getUnrealizedGain();
                String gainColored = gain >= 0
                        ? Colors.ANSI_GREEN + String.format("%+12.2f", gain) + Colors.RESET
                        : Colors.ANSI_RED   + String.format("%12.2f",  gain) + Colors.RESET;

                ConsolePrinter.printMenuOption(String.format(
                        "%-10s %-20s %6d %14.2f %14.2f %8.2f%% %13s %s",
                        bond.getTicker(),
                        bond.getName(),
                        position.getQuantity(),
                        position.getAverageBuyPrice(),
                        bond.getPrice(),
                        bond.getCouponRate(),
                        bond.getMaturityDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        gainColored));
            }
            System.out.println();
        }

        // --- Summary ---
        ConsolePrinter.printMenuTitle("────────────────────────────────────────── Portfolio Summary ──────────────────────────────────────");
        double cashBalance   = user.getCashBalance();
        double totalHoldings = user.getPortfolio().getTotalValue();
        double totalValue    = cashBalance + totalHoldings;
        double totalGain     = user.getPortfolio().getTotalGain();
        ConsolePrinter.printMenuOption("Total Holdings: " + ColorFormatter.conditionalAmountColor(totalHoldings)
                + Colors.MENUOPTION + " | Total Value: " + ColorFormatter.conditionalAmountColor(totalValue)
                + Colors.MENUOPTION + " | Total Gain: " + ColorFormatter.conditionalAmountColor(totalGain));
        ConsolePrinter.printMenuOption("Current Cash Balance: " + ColorFormatter.conditionalAmountColor(cashBalance));
    }

    /**
     * Prompts the user to choose between buying a stock or a bond,
     * then routes to the appropriate buy method.
     */
    private void buyAsset() {
        System.out.println();
        ConsolePrinter.printMenuHeader("What would you like to buy?");
        ConsolePrinter.printMenuOption("1. Stock");
        ConsolePrinter.printMenuOption("2. Bond");
        ConsolePrinter.printMenuOption("0. Cancel");
        ConsolePrinter.printSeparator();

        int choice = MenuChoiceValidator.readChoice(scanner, 0, 2, "cancel");
        switch (choice) {
            case 1 -> buyStock();
            case 2 -> buyBond();
            case 0 -> {
                ConsolePrinter.printConfirmation("Purchase cancelled.");
                show();
            }
        }
    }


    /**
     * Handles the flow for when a user wants to buy a stock.
     * <p>
     * The method shows the market, asks the user to enter a ticker and quantity for the wanted stock, shows a trade summary, and asks for confirmation before completing the purchase.
     * </p>
     *      * The user can cancel the purchase by pressing 0 when asked for a ticker.
     * The {@link Transaction} is then saved to the transactions csv file.
     * </p>
     * Invalid tickers, invalid quantities, insufficient funds or other buy errors are handled by displaying an error message.
     * The user is also given the option to buy another stock.
     * When the flow is done the user is sent back to see the club member menu.
     */
    private void buyStock() {
        while (true) {
            marketService.viewMarket();

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

            ConsolePrinter.printMenuOption("Enter quantity for " + stock.getTicker() + ":");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid quantity. Please try again.");
                continue;
            }

            if (quantity <= 0){
                ConsolePrinter.printError("Quantity must be greater than 0.");
                continue;
            }

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

            ConsolePrinter.printMenuOption("Confirm purchase? (y / n):");
            String confirmation = scanner.nextLine().trim().toLowerCase();

            if (!confirmation.equals("y")) {
                ConsolePrinter.printError("Purchase cancelled. Starting over...");
                continue;
            }

            try {
                Transaction transaction = portfolioService.buy(user, stock, quantity);
                CSVWriter.append(AppConstants.TRANSACTIONS_FILE, transaction);
                ConsolePrinter.printConfirmation("Purchase completed!");
            } catch (Exception e) {
                ConsolePrinter.printError(e.getMessage());
                continue;
            }

            ConsolePrinter.printMenuOption("Buy another stock? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                start();
                return;
            }
        }
    }

    /**
     * Handles the flow for when a user wants to buy a bond.
     * <p>
     *     Shows the bond market, asks for ticker and quantity, displays a trade summary
     *     including coupon rate and maturity date, and asks for confirmation before
     *     completing the purchase.
     *     The {@link Transaction} is saved to the transactions CSV file on confirmation.
     * </p>
     * <p>
     *     The user can cancel by pressing 0 when asked for a ticker.
     *     Invalid tickers, quantities, or insufficient funds are handled with error messages.
     * </p>
     */
    private void buyBond() {
        while (true) {
            bondMarketService.viewMarket();

            ConsolePrinter.printMenuOption("Please enter ticker:    |   Or press 0 to cancel");
            String ticker = scanner.nextLine().trim().toUpperCase();

            if (ticker.equals("0")) {
                ConsolePrinter.printConfirmation("Purchase cancelled");
                show();
                return;
            }

            Bond bond = bondMarketService.findByTicker(ticker);
            if (bond == null) {
                ConsolePrinter.printError("No bond found with ticker: " + ticker + ". Please try again.");
                continue;
            }

            ConsolePrinter.printMenuOption("Enter quantity for " + bond.getTicker() + ":");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid quantity. Please try again.");
                continue;
            }

            if (quantity <= 0) {
                ConsolePrinter.printError("Quantity must be greater than 0.");
                continue;
            }

            double pricePerBond         = bond.getPrice();
            double totalCost            = pricePerBond * quantity;
            double balanceAfterPurchase = user.getCashBalance() - totalCost;

            ConsolePrinter.printMenuHeader("TRADE SUMMARY");
            ConsolePrinter.printSeparator();
            System.out.println("Ticker:                 " + bond.getTicker());
            System.out.println("Name:                   " + bond.getName());
            System.out.printf("Coupon Rate:            %.2f%%%n", bond.getCouponRate());
            System.out.println("Maturity Date:          "
                    + bond.getMaturityDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            System.out.println("Quantity:               " + quantity);
            System.out.printf("Price per bond:         %.2f DKK%n", pricePerBond);
            System.out.printf("Total cost:             %.2f DKK%n", totalCost);
            System.out.printf("Balance after purchase: %.2f DKK%n", balanceAfterPurchase);
            ConsolePrinter.printSeparator();

            ConsolePrinter.printMenuOption("Confirm purchase? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                ConsolePrinter.printError("Purchase cancelled. Starting over...");
                continue;
            }

            try {
                Transaction transaction = portfolioService.buy(user, bond, quantity);
                CSVWriter.append(AppConstants.TRANSACTIONS_FILE, transaction);
                ConsolePrinter.printConfirmation("Purchase completed!");
            } catch (Exception e) {
                ConsolePrinter.printError(e.getMessage());
                continue;
            }

            ConsolePrinter.printMenuOption("Buy another bond? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                show();
                return;
            }
        }
    }


    /**
     * Prompts the user to choose between selling a stock or a bond,
     * then routes to the appropriate sell method.
     */
    private void sellAsset() {
        System.out.println();
        ConsolePrinter.printMenuHeader("What would you like to sell?");
        ConsolePrinter.printMenuOption("1. Stock");
        ConsolePrinter.printMenuOption("2. Bond");
        ConsolePrinter.printMenuOption("0. Cancel");
        ConsolePrinter.printSeparator();

        int choice = MenuChoiceValidator.readChoice(scanner, 0, 2, "cancel");
        switch (choice) {
            case 1 -> sellStock();
            case 2 -> sellBond();
            case 0 -> {
                ConsolePrinter.printConfirmation("Sale cancelled.");
                show();
            }
        }
    }



    /**
     * Handles the flow for when a user wants to sell a stock.
     * <p>
     * The method show the user's portfolio, asks the user to enter a ticker and quantity for the stock they want to sell,
     * shows a trade summary, and asks for confirmation before completing the sale.
     * </p>
     * <p>
     * The user can cancel the sale by pressing 0 when asked for a ticker.
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

            printPortfolio();

            if (user.getPortfolio().getPositions().isEmpty()) {
                return;
            }

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

            ConsolePrinter.printMenuOption("Confirm sale? (y / n):");
            String confirm = scanner.nextLine().trim().toLowerCase();

            if (!confirm.equals("y")) {
                ConsolePrinter.printError("Sale cancelled. Starting over...");
                continue;
            }

            try {
                Transaction transaction = portfolioService.sell(user, ticker, quantity);
                CSVWriter.append(AppConstants.TRANSACTIONS_FILE, transaction);
                ConsolePrinter.printConfirmation("Sale complete!");
            } catch (Exception e) {
                ConsolePrinter.printError(e.getMessage());
                continue;
            }

            ConsolePrinter.printMenuOption("Sell another stock? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                show();
                return;
            }
        }
    }

    /**
     * Handles the flow for when a user wants to sell a bond.
     * <p>
     *     Shows the user's portfolio, asks for ticker and quantity, displays an order summary
     *     including coupon rate and maturity date, and asks for confirmation before completing the sale.
     *     Shows a warning if the bond has not yet reached its maturity date.
     *     The {@link Transaction} is saved to the transactions CSV file on confirmation.
     * </p>
     * <p>
     *     The user can cancel by pressing 0 when asked for a ticker.
     *     Invalid tickers, quantities, or insufficient holdings are handled with error messages.
     * </p>
     */
    private void sellBond() {
        while (true) {
            printPortfolio();

            if (user.getPortfolio().getPositions().isEmpty()) {
                show();
                return;
            }

            Position position = null;
            String ticker = "";

            while (position == null) {
                ConsolePrinter.printMenuOption("Enter ticker of the bond you would like to sell:   |   Or press 0 to cancel");
                ticker = scanner.nextLine().trim().toUpperCase();

                if (ticker.equals("0")) {
                    ConsolePrinter.printConfirmation("Sale cancelled");
                    show();
                    return;
                }

                position = user.getPortfolio().findByTicker(ticker);

                // make sure the position is actually a bond
                if (position != null && !(position.getAsset() instanceof Bond)) {
                    ConsolePrinter.printError(ticker + " is not a bond. Please try again.");
                    position = null;
                    continue;
                }

                if (position == null) {
                    ConsolePrinter.printError("You do not own a bond with that ticker. Please try again.");
                }
            }

            ConsolePrinter.printMenuOption("Enter quantity:");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                ConsolePrinter.printError("Invalid quantity. Please try again.");
                continue;
            }

            if (quantity > position.getQuantity()) {
                ConsolePrinter.printError("You only own " + position.getQuantity()
                        + " bonds of " + ticker + ". Starting over...");
                continue;
            }

            Bond bond = (Bond) position.getAsset();

            // warn if selling before maturity
            if (!bond.isMature()) {
                long daysRemaining = bond.getDaysToMaturity();
                ConsolePrinter.printError("Warning: This bond matures in " + daysRemaining
                        + " days. Selling before maturity may result in a loss.");
                ConsolePrinter.printMenuOption("Are you sure you want to continue? (y / n):");
                if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                    ConsolePrinter.printError("Sale cancelled.");
                    continue;
                }
            }

            double unitPrice     = bond.getPrice();
            double totalProceeds = unitPrice * quantity;
            double projectedCash = user.getCashBalance() + totalProceeds;

            ConsolePrinter.printMenuHeader("ORDER SUMMARY");
            ConsolePrinter.printSeparator();
            ConsolePrinter.printMenuOption("Ticker:          " + ticker);
            ConsolePrinter.printMenuOption("Name:            " + bond.getName());
            ConsolePrinter.printMenuOption("Coupon Rate:     " + bond.getCouponRate() + "%");
            ConsolePrinter.printMenuOption("Maturity Date:   "
                    + bond.getMaturityDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            ConsolePrinter.printMenuOption("Quantity:        " + quantity + " bonds");
            ConsolePrinter.printMenuOption("Unit Price:      " + unitPrice + " DKK");
            ConsolePrinter.printMenuOption("Total Proceeds:  " + totalProceeds + " DKK");
            ConsolePrinter.printMenuOption("Cash After Sale: " + projectedCash + " DKK");
            ConsolePrinter.printSeparator();

            ConsolePrinter.printMenuOption("Confirm sale? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                ConsolePrinter.printError("Sale cancelled. Starting over...");
                continue;
            }

            try {
                Transaction transaction = portfolioService.sell(user, ticker, quantity);
                CSVWriter.append(AppConstants.TRANSACTIONS_FILE, transaction);
                ConsolePrinter.printConfirmation("Sale complete!");
            } catch (Exception e) {
                ConsolePrinter.printError(e.getMessage());
                continue;
            }

            ConsolePrinter.printMenuOption("Sell another bond? (y / n):");
            if (!scanner.nextLine().trim().toLowerCase().equals("y")) {
                show();
                return;
            }
        }
    }


    /**
     * Displays the current stock market and returns the user to the member menu after wards.
     */
    private void viewMarket() {
        marketService.viewMarket();
        show();
    }

    /**
     * Displays the logged-in user's full transaction history sorted newest first.
     * <p>
     *     Retrieves all transactions via {@link PortfolioService#getTransactionHistory(User)} and
     *     prints them in a formatted 100-character wide table showing date, order type (BUY/SELL),
     *     ticker, quantity, and price per share in DKK.
     *     If the user has no transactions, an informational message is shown instead.
     * </p>
     * <p>
     *     Returns the user to the Club Member menu when done.
     * </p>
     */
    private void viewTransactions() {
        System.out.println();
        ConsolePrinter.printMenuTitle("──────────────────────────────────────── Transaction History ───────────────────────────────────────");
        List<Transaction> history = portfolioService.getTransactionHistory(user);
        if (history.isEmpty()) {
            ConsolePrinter.printError("No transactions found.");
        } else {
            System.out.printf("%-12s %-8s %-46s %10s %20s%n", "DATE", "TYPE", "TICKER", "QTY", "PRICE/SHARE (DKK)");
            ConsolePrinter.printSeparator();
            for (Transaction t : history) {
                ConsolePrinter.printMenuOption(String.format("%-12s %-8s %-46s %10d %20.2f",
                        t.getDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        t.getOrderType(),
                        t.getTicker(),
                        t.getQuantity(),
                        t.getPrice()
                ));
            }
        }
        show();
    }


    /**
     * Handles the search stock flow.
     * <p>
     *     Prompts the user to enter a search term and passes it to
     *     {@link StockMarketService#searchStocks(String)}, which matches against
     *     ticker symbol, company name, and sector. The results are displayed
     *     in a formatted market table. If no matches are found, am error message
     *     is shown instead.
     *
     *     Returns the user to the menu whe done.
     * </p>
     *
     */
    private void searchStocks(){
        ConsolePrinter.printMenuOption("Search by ticker, name or sector: ");
        String searchInput = scanner.nextLine();
        Collection<Stock> results = marketService.searchStocks(searchInput);

        if (results.isEmpty()){
            ConsolePrinter.printError("No stock found.");
        } else {
            marketService.viewMarket(results);
        }
        show();
    }
}