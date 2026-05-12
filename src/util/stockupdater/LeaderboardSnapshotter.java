package util.stockupdater;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import util.constants.AppConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardSnapshotter {

    private final StockMarketService marketService;
    private final PortfolioService portfolioService;
    private final UserService userService;
    private final String transactionsFile;

    public LeaderboardSnapshotter(StockMarketService marketService,
                                  PortfolioService portfolioService,
                                  UserService userService) {
        this(marketService, portfolioService, userService, AppConstants.TRANSACTIONS_FILE);
    }

    public LeaderboardSnapshotter(StockMarketService marketService,
                                  PortfolioService portfolioService,
                                  UserService userService,
                                  String transactionsFile) {
        this.marketService = marketService;
        this.portfolioService = portfolioService;
        this.userService = userService;
        this.transactionsFile = transactionsFile;
    }


    public void snapShotAt(){

    }


    public void snapshotAt(int year, int month, int day) {
        try {
            StockPriceUpdater.updatePricesForDate(year, month, day);
            marketService.reload(AppConstants.STOCK_MARKET_FILE);
            portfolioService.resetPortfolios(userService.getAllUsers());
            userService.getAllUsers().forEach(u -> portfolioService.loadPortfolio(u, transactionsFile));

            System.out.println("\n===== LEADERBOARD: " + day + "-" + month + "-" + year + " =====");

            List<User> sorted = new ArrayList<>(userService.getAllUsers());
            sorted.sort((a, b) -> {
                double totalA = a.getCashBalance() + a.getPortfolio().getTotalValue();
                double totalB = b.getCashBalance() + b.getPortfolio().getTotalValue();
                return Double.compare(totalB, totalA); // descending
            });

            for (User u : sorted) {
                double total = u.getCashBalance() + u.getPortfolio().getTotalValue();
                System.out.printf("%-20s %,.2f DKK%n", u.getFullName(), total);
            }

        } catch (Exception e) {
            System.out.println("Failed to snapshot leaderboard for "
                    + day + "-" + month + "-" + year + ": " + e.getMessage());
        }
    }
}