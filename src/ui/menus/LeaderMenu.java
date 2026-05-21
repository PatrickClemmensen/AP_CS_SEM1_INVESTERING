package ui.menus;

import interfaces.Tradeable;
import model.asset.Stock;
import model.portfolio.Position;
import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import ui.enums.LeaderOption;
import util.comparator.ByPercentReturn;
import util.constants.Colors;
import util.printing.ConsolePrinter;
import util.validation.MenuChoiceValidator;

import java.util.*;

/**
 * Handles the menu flow and user interactions for a logged-in club leader.
 * <p>
 *     A club leader has exclusive access to the following features:
 *     <ul>
 *         <li>View all members with their cash, holdings, and total wealth</li>
 *         <li>View the member leaderboard sorted by total value or percentage return</li>
 *         <li>View the club's stock distribution across individual tickers</li>
 *         <li>View the club's sector distribution across all holdings</li>
 *         <li>Search for members by name</li>
 *     </ul>
 * </p>
 */
public class LeaderMenu {
    private final StockMarketService marketService;
    private final PortfolioService portfolioService;
    private final UserService userService;
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a {@code LeaderMenu} backed by the provided services.
     *
     * @param marketService    service for accessing stock market data
     * @param portfolioService service for loading and querying member portfolios
     * @param userService      service for accessing and searching user accounts
     */
    public LeaderMenu(StockMarketService marketService, PortfolioService portfolioService,
                      UserService userService) {
        this.marketService    = marketService;
        this.portfolioService = portfolioService;
        this.userService      = userService;
    }

    /**
     * Initiation of the menu process - displays the menu and loops until the user chooses to exit.
     * Also contains the logic for exiting the menu.
     */
    public void start() {
        show();
        while (true) {
            try {
                int input = MenuChoiceValidator.readChoice(scanner, 0, 5, "logout");
                LeaderOption option = LeaderOption.fromChoice(input);
                if (option == LeaderOption.EXIT) {
                    ConsolePrinter.printConfirmation("Logout successful!");
                    break;
                }
                handleChoice(option);
            } catch (IllegalArgumentException e) {
                ConsolePrinter.printError("Invalid choice, please try again.");
            }
        }
    }

    /**
     * Prints the Club Leader menu.
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
     * Directs the leader to a new submenu based on the chosen option.
     *
     * @param option choice made by the logged-in leader
     */
    private void handleChoice(LeaderOption option) {
        switch (option) {
            case OPTION_1 -> viewAllMembers();
            case OPTION_2 -> viewLeaderboard();
            case OPTION_3 -> viewStockDistribution();
            case OPTION_4 -> viewSectorDistribution();
            case OPTION_5 -> searchMembers();
        }
    }

    // ─────────────────────────────────────────── MEMBERS ───────────────────────────────────────────

    /**
     * Displays a table of all registered members sorted by user ID,
     * showing each member's cash balance, holdings value, and total wealth in DKK.
     */
    private void viewAllMembers() {
        List<User> members = new ArrayList<>(userService.getAllUsers());
        members.sort(Comparator.comparingInt(User::getUserId));

        for (User member : members) {
            portfolioService.loadPortfolio(member);
        }

        System.out.println();
        ConsolePrinter.printMenuTitle("──────────────────────────────────────────── All Members ──────────────────────────────────────────");
        System.out.printf(Colors.MENUHEADER + "%-5s %-25s %18s %18s %18s%n" + Colors.RESET,
                "ID", "NAME", "CASH (DKK)", "HOLDINGS (DKK)", "TOTAL (DKK)");
        ConsolePrinter.printSeparator();

        for (User member : members) {
            double cash     = member.getCashBalance();
            double holdings = member.getPortfolio().getTotalValue();
            double total    = cash + holdings;
            System.out.printf(Colors.MENUOPTION + "%-5d %-25s %18.2f %18.2f %18.2f%n" + Colors.RESET,
                    member.getUserId(), member.getFullName(), cash, holdings, total);
        }

        ConsolePrinter.printSeparator();
        show();
    }

    /**
     * Displays a leaderboard of all members ranked by total wealth or percentage return.
     * <p>
     *     The leader chooses the sort strategy:
     *     <ul>
     *         <li>Total value — uses {@link Comparable} on {@link User}</li>
     *         <li>Percentage return — uses {@link util.comparator.ByPercentReturn} {@link java.util.Comparator}</li>
     *     </ul>
     * </p>
     */
    private void viewLeaderboard() {
        List<User> members = new ArrayList<>(userService.getAllUsers());

        for (User member : members) {
            portfolioService.loadPortfolio(member);
        }

        // --- sort selection ---
        ConsolePrinter.printMenuHeader("Sort leaderboard by:");
        ConsolePrinter.printMenuOption("1. Total value (DKK)");
        ConsolePrinter.printMenuOption("2. Percentage return");
        ConsolePrinter.printSeparator();

        int choice = MenuChoiceValidator.readChoice(scanner, 1, 2, "cancel");

        if (choice == 1) {
            Collections.sort(members);           // Comparable — total value descending
        } else {
            members.sort(new ByPercentReturn()); // Comparator<Rankable> — percent return descending
        }

        // --- display ---
        System.out.println();
        ConsolePrinter.printMenuTitle("─────────────────────────────────────────── Leaderboard ───────────────────────────────────────────");
        System.out.printf(Colors.MENUHEADER + "%-5s %-25s %20s %20s%n" + Colors.RESET,
                "RANK", "NAME", "TOTAL VALUE (DKK)", "RETURN (%)");
        ConsolePrinter.printSeparator();

        int rank = 1;
        for (User member : members) {
            double total     = member.getRankValue();
            double returnPct = ((total - member.getInitialCash()) / member.getInitialCash()) * 100;
            System.out.printf(Colors.MENUOPTION + "%-5d %-25s %20.2f %19.2f%%%n" + Colors.RESET,
                    rank++, member.getFullName(), total, returnPct);
        }

        ConsolePrinter.printSeparator();
        show();
    }

    // ─────────────────────────────────────────── DISTRIBUTION ───────────────────────────────────────────

    /**
     * Displays how the club's total invested value is distributed across individual stocks.
     * <p>
     *     Each ticker is shown with its DKK value and percentage share of all holdings,
     *     sorted descending by percentage. Cash balances are excluded.
     * </p>
     */
    private void viewStockDistribution() {
        List<User> members = new ArrayList<>(userService.getAllUsers());
        for (User member : members) {
            portfolioService.loadPortfolio(member);
        }

        Map<String, Double> valueByTicker = new LinkedHashMap<>();
        for (User member : members) {
            for (Position position : member.getPortfolio().getPositions()) {
                String ticker = position.getAsset().getTicker();
                double value  = position.getCurrentValue();
                valueByTicker.merge(ticker, value, Double::sum);
            }
        }

        if (valueByTicker.isEmpty()) {
            ConsolePrinter.printError("No active positions found across any member portfolio.");
            show();
            return;
        }

        double totalInvested = valueByTicker.values().stream()
                .mapToDouble(Double::doubleValue).sum();

        List<Map.Entry<String, Double>> sorted = new ArrayList<>(valueByTicker.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.println();
        ConsolePrinter.printMenuTitle("──────────────────────────────────────── Stock Distribution ────────────────────────────────────────");
        System.out.printf(Colors.MENUHEADER + "%-10s %-40s %18s %10s%n" + Colors.RESET,
                "TICKER", "NAME", "VALUE (DKK)", "SHARE (%)");
        ConsolePrinter.printSeparator();

        for (Map.Entry<String, Double> entry : sorted) {
            String ticker    = entry.getKey();
            double value     = entry.getValue();
            double percentage = (value / totalInvested) * 100;

            // getName() is on Tradeable — works for both stocks and bonds
            Tradeable asset = marketService.findByTicker(ticker);
            String name = (asset != null) ? asset.getName() : ticker;

            System.out.printf(Colors.MENUOPTION + "%-10s %-40s %18.2f %9.2f%%%n" + Colors.RESET,
                    ticker, name, value, percentage);
        }

        ConsolePrinter.printSeparator();
        System.out.printf(Colors.MENUOPTION + "%-10s %-40s %18.2f %10s%n" + Colors.RESET,
                "", "TOTAL", totalInvested, "100.00%");
        ConsolePrinter.printSeparator();
        show();
    }

    /**
     * Displays how the club's total invested value is distributed across sectors.
     * <p>
     *     Each sector is shown with its DKK value and percentage share of all holdings,
     *     sorted descending by percentage. Cash balances are excluded.
     *     Non-stock assets (e.g. bonds) are grouped under "Other".
     * </p>
     */
    private void viewSectorDistribution() {
        List<User> members = new ArrayList<>(userService.getAllUsers());
        for (User member : members) {
            portfolioService.loadPortfolio(member);
        }

        Map<String, Double> valueBySector = new LinkedHashMap<>();
        for (User member : members) {
            for (Position position : member.getPortfolio().getPositions()) {
                // instanceof pattern matching — works for Tradeable field on Position
                String sector = (position.getAsset() instanceof Stock stock)
                        ? stock.getSector()
                        : "Other";
                double value = position.getCurrentValue();
                valueBySector.merge(sector, value, Double::sum);
            }
        }

        if (valueBySector.isEmpty()) {
            ConsolePrinter.printError("No active positions found across any member portfolios.");
            show();
            return;
        }

        double totalInvested = valueBySector.values().stream()
                .mapToDouble(Double::doubleValue).sum();

        List<Map.Entry<String, Double>> sorted = new ArrayList<>(valueBySector.entrySet());
        sorted.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

        System.out.println();
        ConsolePrinter.printMenuTitle("────────────────────────────────────────── Sector Distribution ────────────────────────────────────");
        System.out.printf(Colors.MENUHEADER + "%-30s %18s %10s%n" + Colors.RESET,
                "SECTOR", "VALUE (DKK)", "SHARE (%)");
        ConsolePrinter.printSeparator();

        for (Map.Entry<String, Double> entry : sorted) {
            String sector    = entry.getKey();
            double value     = entry.getValue();
            double percentage = (value / totalInvested) * 100;
            System.out.printf(Colors.MENUOPTION + "%-30s %18.2f %9.2f%%%n" + Colors.RESET,
                    sector, value, percentage);
        }

        ConsolePrinter.printSeparator();
        System.out.printf(Colors.MENUHEADER + "%-30s %18.2f %10s%n" + Colors.RESET,
                "TOTAL", totalInvested, "100.00%");
        ConsolePrinter.printSeparator();
        show();
    }

    // ─────────────────────────────────────────── SEARCH ───────────────────────────────────────────

    /**
     * Allows the leader to search for members by full name.
     * <p>
     *     Results are displayed in a formatted member table.
     *     If no matches are found, an error message is shown.
     * </p>
     */
    private void searchMembers() {
        ConsolePrinter.printMenuOption("Search for member by name: ");
        String searchInput = scanner.nextLine();

        Collection<User> results = userService.searchMembers(searchInput);

        if (results.isEmpty()) {
            ConsolePrinter.printError("No member found.");
        } else {
            printMembers(results);
        }
        show();
    }

    /**
     * Prints a formatted table of the given members.
     *
     * @param members the members to display
     */
    private void printMembers(Collection<User> members) {
        System.out.println();
        ConsolePrinter.printMenuTitle("──────────────────────────────────────────── Members ──────────────────────────────────────────────");
        System.out.printf(Colors.MENUHEADER + "%-5s %-25s %18s %18s %18s%n" + Colors.RESET,
                "ID", "NAME", "CASH (DKK)", "HOLDINGS (DKK)", "TOTAL (DKK)");
        ConsolePrinter.printSeparator();

        for (User member : members) {
            portfolioService.loadPortfolio(member);

            double cash     = member.getCashBalance();
            double holdings = member.getPortfolio().getTotalValue();
            double total    = cash + holdings;

            System.out.printf(Colors.MENUOPTION + "%-5d %-25s %18.2f %18.2f %18.2f%n" + Colors.RESET,
                    member.getUserId(), member.getFullName(), cash, holdings, total);
        }

        ConsolePrinter.printSeparator();
    }
}