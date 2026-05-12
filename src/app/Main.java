package app;

import model.asset.Stock;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import ui.MainMenu;
import util.AppConstants;

import javax.sound.sampled.Port;

public class Main {
    public static void main(String[] args) {
        // TODO: instantiate services with the correct file paths
        UserService userService = new UserService(AppConstants.USERS_FILE);
        StockMarketService marketService = new StockMarketService(AppConstants.STOCK_MARKET_FILE);
        PortfolioService portfolioService = new PortfolioService(marketService);
        // TODO: pass them to MainMenu and call start()
        new MainMenu(userService, marketService,portfolioService).start();

    }
}