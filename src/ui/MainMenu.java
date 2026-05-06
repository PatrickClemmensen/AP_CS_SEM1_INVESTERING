package ui;

import service.PortfolioService;
import service.StockMarketService;
import service.UserService;

public class MainMenu {
    // TODO: declare references to services

    public MainMenu(UserService userService, StockMarketService marketService,
                    PortfolioService portfolioService) {
        // TODO: initialize fields
    }

    public void start() {
        // TODO: display a welcome message and login prompt
        // TODO: ask for a user ID and look up the user via UserService
        // TODO: if found, pass the user to MemberMenu and call its start() method
        // TODO: loop until the user chooses to exit
    }
}