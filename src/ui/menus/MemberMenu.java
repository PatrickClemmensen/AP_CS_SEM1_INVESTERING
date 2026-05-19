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
    private User user;
    private StockMarketService marketService;
    private PortfolioService portfolioService;
    private Scanner scanner = new Scanner(System.in);

    /**
     * Creates a MemberMenu for the given user, backed by the provided services.
     *
     * @param user the logged-in user
     * @param marketService service for accessing stock market data
     * @param portfolioService service for loading and managing the user's portfolio
     */
    public MemberMenu(User user, StockMarketService marketService, PortfolioService portfolioService) {
        this.user = user;
        this.marketService = marketService;
        this.portfolioService = portfolioService;
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
     * @param option the menu option chosen by the logged-in user
     */
    private void handleChoice(MemberOption option) {
        switch (option) {
            case OPTION_1 -> viewPortfolio();
            case OPTION_2 -> buyStock();
            case OPTION_3 -> sellStock();
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
        System.out.printf("%-10s %-29s %8s %16s %16s %16s%n", "TICKER", "NAME", "QTY", "AVG BUY", "VALUE", "POT. GAIN");
        ConsolePrinter.printSeparator();

        if (user.getPortfolio().getPositions().isEmpty()) {
            ConsolePrinter.printError("Your portfolio is empty.");
        } else {
            for (Position position : user.getPortfolio().getPositions()) {
                ConsolePrinter.printMenuOption(position.toString());
            }
            System.out.println();
            ConsolePrinter.printMenuTitle("────────────────────────────────────────── Portfolio Summary ──────────────────────────────────────");
            double cashBalance = user.getCashBalance();
            double totalHoldings = user.getPortfolio().getTotalValue();
            double totalValue = user.getCashBalance() + user.getPortfolio().getTotalValue();
            double totalGain = user.getPortfolio().getTotalGain();
            ConsolePrinter.printMenuOption("Total Holdings: " + ColorFormatter.conditionalAmountColor(totalHoldings) + Colors.MENUOPTION + " | Total Value: " + ColorFormatter.conditionalAmountColor(totalValue) + Colors.MENUOPTION + " | Total Gain: " + ColorFormatter.conditionalAmountColor(totalGain));
            ConsolePrinter.printMenuOption("Current Cash Balance: " + ColorFormatter.conditionalAmountColor(cashBalance) );

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
     * Handles the flow for when a user wants to buy a stock.
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
    private void searchStocks(){
        ConsolePrinter.printMenuOption("Search by ticker, navn or sector: ");
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