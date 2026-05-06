package ui;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import util.printing.ConsolePrinter;

import java.util.Scanner;

public class MainMenu {
    // TODO: declare references to services
    private UserService userService;
    private StockMarketService marketService;
    private PortfolioService portfolioService;

    public MainMenu(UserService userService, StockMarketService marketService,
                    PortfolioService portfolioService) {
        // TODO: initialize fields
        this.userService = userService;
        this.marketService = marketService;
        this.portfolioService = portfolioService;
    }

    public void start() {
        // TODO: display a welcome message and login prompt
        // TODO: ask for a user ID and look up the user via UserService
        // TODO: if found, pass the user to MemberMenu and call its start() method
        // TODO: loop until the user chooses to exit
        Scanner scanner = new Scanner(System.in);
        ConsolePrinter.printMenuHeader("Welcome message");
        while(true){
            ConsolePrinter.printMenuOption("Write user id:");

            try {
                User user = userService.findById(Integer.valueOf(scanner.nextLine()));
                if(user != null){
                    new MemberMenu(user,marketService,portfolioService).start();

                }else{
                    System.out.println("user not found");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}