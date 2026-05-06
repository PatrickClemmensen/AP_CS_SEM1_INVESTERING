package app;

import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import ui.MainMenu;
import util.AppConstants;

public class Main {
    public static void main(String[] args) {
        // TODO: instantiate services with the correct file paths
        // TODO: pass them to MainMenu and call start()
        UserService userService = new UserService(AppConstants.USERS_FILE);
        StockMarketService marketService = new StockMarketService(AppConstants.STOCK_MARKET_FILE);
        PortfolioService portfolioService = new PortfolioService(marketService);
        new MainMenu(userService,marketService,portfolioService).start();
    }
}