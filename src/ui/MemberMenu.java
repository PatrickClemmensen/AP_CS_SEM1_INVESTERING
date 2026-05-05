package ui;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;

public class MemberMenu {
    // TODO: declare references to user, marketService, portfolioService

    public MemberMenu(User user, StockMarketService marketService,
                      PortfolioService portfolioService) {
        // TODO: initialize fields
    }

    public void start() {
        // TODO: display a menu with options, e.g:
        //   1. View portfolio
        //   2. Buy stock
        //   3. Sell stock
        //   4. View market
        //   5. Logout
        // TODO: read user input and route to the appropriate method
    }

    private void viewPortfolio() {
        // TODO: print all positions in the user's portfolio
        // TODO: show total value and total unrealized gain
    }

    private void buyStock() {
        // TODO: prompt for ticker and quantity
        // TODO: call portfolioService.buy() and handle any exceptions
    }

    private void sellStock() {
        // TODO: prompt for ticker and quantity
        // TODO: call portfolioService.sell() and handle any exceptions
    }

    private void viewMarket() {
        // TODO: print all available stocks from marketService.getAllStocks()
    }
}