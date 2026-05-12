package app;

import model.asset.Stock;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import ui.menus.MainMenu;
import util.constants.AppConstants;
import util.stockupdater.LeaderboardSnapshotter;

public class Main {
    public static void main(String[] args) {

        UserService userService = new UserService(AppConstants.USERS_FILE);
        StockMarketService marketService = new StockMarketService(AppConstants.STOCK_MARKET_FILE);
        PortfolioService portfolioService = new PortfolioService(marketService);


        //new MainMenu(userService, marketService,portfolioService).start();


        // Demo snapshots
        LeaderboardSnapshotter snapshotter = new LeaderboardSnapshotter(
                marketService, portfolioService, userService,
                "data/dummy_transactions.csv"  // ← swapped here, nothing else changes
        );
        snapshotter.snapshotAt(2025, 4, 22);
        snapshotter.snapshotAt(2025, 5, 1);
        snapshotter.snapshotAt(2025, 7, 1);



    }
}