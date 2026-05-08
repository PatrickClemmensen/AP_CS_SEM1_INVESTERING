package ui;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import util.printing.ConsolePrinter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

    public class MainMenu {
    // TODO: declare references to services
    private UserService userService;
    private StockMarketService marketService;
    private PortfolioService portfolioService;
    /**
    *  Scanner used to read input from the console for all methods.
    */
    Scanner scanner = new Scanner(System.in);

    /**
     * @param userService is used to find users.
     * @param marketService is used to access the stock market.
     * @param portfolioService is used to access portfolio functionalities.
     */
    public MainMenu(UserService userService, StockMarketService marketService,
                    PortfolioService portfolioService) {
        // TODO: initialize fields
        this.userService = userService;
        this.marketService = marketService;
        this.portfolioService = portfolioService;
    }

    /**
     * Starts the main menu.
     * <p>
     * The methos repeatedly asks the user to enter a user ID
     *If the written ID belongs to an existing user, the user is sent to the member menu.
     * If the ID is not found, then the user is shown the registration prompt from userNotFound() method.
     */
    public void start() {

        // TODO: display a welcome message and login prompt
        ConsolePrinter.printMenuHeader("Welcome message");
        // TODO: loop until the user chooses to exit

        while (true) {
            // TODO: ask for a user ID and look up the user via UserService
            ConsolePrinter.printMenuOption("Write user id:");
            // TODO: if found, pass the user to MemberMenu and call its start() method

            try {
                User user = userService.findById(Integer.valueOf(scanner.nextLine()));
                if (user != null) {
                    //logic to differentiate between member and club leader goes here:
                    new MemberMenu(user, marketService, portfolioService).start();
                } else {
                    //AC2 criteria goes here
                    userNotFound();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
    /**
     * Handles what happens, when a user is not found.
     * <p>
     * The method asks whether they want to register.
     * If "yes", the registration logic should be executed
     * If "no", the method exits the method and the user is returned to the ID prompt in the main menu.
     * Invalid input causes the method to start over and ask again.
     */
    private void userNotFound() {

        ConsolePrinter.printError("User not found");
        while (true) {
            ConsolePrinter.printMenuOption("Register? | yes or no");

            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("yes")) {
                //register user here
                    Scanner scanner = new Scanner(System.in);
                    ConsolePrinter.printMenuOption("Register new user. Please enter the required information.");

                    ConsolePrinter.printMenuOption("Full name: ");
                    String fullName = scanner.nextLine();

                    ConsolePrinter.printMenuOption("Email: ");
                    String email = scanner.nextLine();

                    ConsolePrinter.printMenuOption("Enter birth date (dd-MM-yyyy): ");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate birthDate = LocalDate.parse(scanner.nextLine(), formatter);

                    int newUserId = userService.getAllUsers().stream()
                            .mapToInt(User::getUserId)
                            .max()
                            .orElse(0) + 1;

                LocalDate createdAt = LocalDate.now();
                User newUser = new User(newUserId, fullName, email, birthDate, 100000.0, createdAt, createdAt);

                userService.addUser(newUser);
                break;
            } else if (choice.equals("no")) {
                System.out.println("User not found. Please enter another id to login.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again with yes or no.");

            }
        }
    }
}