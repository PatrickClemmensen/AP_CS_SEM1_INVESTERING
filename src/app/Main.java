package app;

import model.asset.Stock;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import ui.menus.MainMenu;
import util.AppConstants;
import util.benchmark.FileReadBenchmark;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        //FileReadBenchmark.run(AppConstants.STOCK_MARKET_FILE, 1000);
        //FileReadBenchmark.run(AppConstants.USERS_FILE, 1000);
        //FileReadBenchmark.run(AppConstants.TRANSACTIONS_FILE, 1000);

        UserService userService = new UserService(AppConstants.USERS_FILE);
        StockMarketService marketService = new StockMarketService(AppConstants.STOCK_MARKET_FILE);
        PortfolioService portfolioService = new PortfolioService(marketService);

        new MainMenu(userService, marketService,portfolioService).start();

    }
}