package ui.menus;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import ui.enums.LeaderOption;
import util.constants.Colors;
import util.printing.ConsolePrinter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class LeaderMenu {
    private StockMarketService marketService;
    private PortfolioService portfolioService;
    private UserService userService;
    private Scanner scanner = new Scanner(System.in);

    public LeaderMenu(StockMarketService marketService, PortfolioService portfolioService, UserService userService) {
        this.marketService = marketService;
        this.portfolioService = portfolioService;
        this.userService = userService;
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
                    ConsolePrinter.printConfirmation("Logout succesful!");
                    break;
                }
                handleChoice(option);
            } catch (IllegalArgumentException e) {
                ConsolePrinter.printError("Invalid choice, please try again.");
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
            case OPTION_1 -> viewAllMembers();
            case OPTION_2 -> viewLeaderboard();
        }
    }

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
            double cash = member.getCashBalance();
            double holdings = member.getPortfolio().getTotalValue();
            double total = cash + holdings;
            System.out.printf(Colors.MENUOPTION + "%-5d %-25s %18.2f %18.2f %18.2f%n" + Colors.RESET,
                    member.getUserId(), member.getFullName(), cash, holdings, total);
        }

        ConsolePrinter.printSeparator();
        show();
    }

    private void viewLeaderboard() {
        List<User> members = new ArrayList<>(userService.getAllUsers());

        for (User member : members) {
            portfolioService.loadPortfolio(member);
        }

        System.out.println();
        ConsolePrinter.printMenuTitle("─────────────────────────────────────────── Leaderboard ───────────────────────────────────────────");
        System.out.printf(Colors.MENUHEADER + "%-5s %-25s %9s%n" + Colors.RESET,
                "RANK", "NAME", "TOTAL WEALTH (DKK)");
        ConsolePrinter.printSeparator();

        int[] rank = {1};
        members.stream()
                .sorted(Comparator.comparingDouble(
                                (User u) -> u.getCashBalance() + u.getPortfolio().getTotalValue())
                        .reversed())
                .limit(10)
                .forEach(member -> {
                    double total = member.getCashBalance() + member.getPortfolio().getTotalValue();
                    System.out.printf(Colors.MENUOPTION + "%-5d %-25s %9.2f%n" + Colors.RESET,
                            rank[0]++, member.getFullName(), total);
                });

        ConsolePrinter.printSeparator();
        show();
    }

}
