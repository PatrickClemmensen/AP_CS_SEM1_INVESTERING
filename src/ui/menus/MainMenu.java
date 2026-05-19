package ui.menus;

import model.portfolio.User;
import service.PortfolioService;
import service.StockMarketService;
import service.UserService;
import ui.enums.MainOption;
import ui.enums.MemberOption;
import util.AppConstants;
import util.constants.Colors;
import util.csv.CSVWriter;
import util.exception.InvalidInputException;
import util.printing.ConsolePrinter;
import util.validation.InitialInvestmentValidator;
import util.validation.MenuChoiceValidator;
import util.validation.PasswordValidator;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static ui.enums.MainOption.OPTION_1;
import static ui.enums.MainOption.OPTION_2;

/**
 * The top-level menu of the Investeringsklubben application.
 * <p>
 *     Presents login options for club members and the club leader,
 *     and handles new member registration when an unknown user ID is entered.
 *     Routes authenticated users to either {@link MemberMenu} or {@link LeaderMenu}.
 * </p>
 */
public class MainMenu {

    private UserService userService;
    private StockMarketService marketService;
    private PortfolioService portfolioService;

    /**
    *  Scanner used to read input from the console for all methods.
    */
    Scanner scanner = new Scanner(System.in);

    /**
     * Creates a {@code MainMenu} backed by the provided services.
     *
     * @param userService is used to find users.
     * @param marketService is used to access the stock market.
     * @param portfolioService is used to access portfolio functionalities.
     */
    public MainMenu(UserService userService, StockMarketService marketService,
                    PortfolioService portfolioService) {
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
        System.out.println();
        ConsolePrinter.printMenuTitle("───────────────────────────────────────── Investeringsklubben ─────────────────────────────────────");
        ConsolePrinter.printMenuHeader("Welcome - please, select an option to log in: ");
        for (MainOption option : MainOption.values()) {
            ConsolePrinter.printMenuOption(option.getValue() + ". " + option.getLabel());
        }
        ConsolePrinter.printSeparator();

        int input = MenuChoiceValidator.readChoice(scanner, 0, 2,"exit the program");
        MainOption option = MainOption.fromChoice(input);

        switch (option) {
            case OPTION_1 -> sendToMemberMenu();
            case OPTION_2 -> sendToLeaderMenu();
            case EXIT -> {
                ConsolePrinter.printConfirmation("\nExiting program... Ses til nævekamp (ง •̀_•́)ง");
                System.exit(0);
            }
        }
    }

    /**
     * Handles the login flow for the club member.
     * <p>
     *     Prompts the user to enter their ID and looks them up via {@link UserService}.
     *     If found, the user is sent to the{@link MemberMenu}.
     *     If not found, the registration prompt is shown via {@link #userNotFound()}.
     * </p>
     */
    private void sendToMemberMenu() {
        while (true) {
            System.out.println();
            ConsolePrinter.printMenuHeader("Logging in as 'Club Member'");
            ConsolePrinter.printMenuOption("Enter User ID:");
            try {
                User user = userService.findById(Integer.valueOf(scanner.nextLine()));
                if (user != null) {
                    new MemberMenu(user, marketService, portfolioService).start();
                    start();
                    break;
                } else {
                    userNotFound();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Handles the login flow for the club Leader.
     * <p>
     *     Prompts the leader password and validates it via {@link PasswordValidator}.
     *     If the password is correct, the leader is sent to the {@link LeaderMenu}.
     *     If incorrect, an error is displayed and the application returns to the main menu.
     * </p>
     */
    private void sendToLeaderMenu() {
        while (true) {
            ConsolePrinter.printMenuHeader("Logging is as 'Club Leader'");
            ConsolePrinter.printMenuOption("Enter password:");
            String password = scanner.nextLine();
            try {
                PasswordValidator.validatePassword(password);
                ConsolePrinter.printConfirmation("Access granted");
                new LeaderMenu(marketService, portfolioService, userService).start();
                start();
                break;
            } catch (InvalidInputException e) {
                ConsolePrinter.printError(e.getMessage());
                start();
                break;
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
            ConsolePrinter.printSeparator();
            ConsolePrinter.printMenuHeader("Would you like to register as a new user?");
            ConsolePrinter.printMenuOption("1. Yes" +
                    "\n2. No");
            ConsolePrinter.printSeparator();

            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("1")) {
                    System.out.println();
                    ConsolePrinter.printMenuTitle("──────────────────────────────────────── Register New User ────────────────────────────────────────");
                    ConsolePrinter.printMenuHeader("Thank you for your interest in Investeringsklubben! You're about to register as a new user and need" +
                            "\nto answer a few questions in order to get access to the platform and start investing.");

                    System.out.println();
                    ConsolePrinter.printMenuTitle("────────────────────────────────── Register New User ─ Step 1/4 ──────────────────────────────────");
                    ConsolePrinter.printMenuOption("Please, enter your full name: ");
                    String fullName = scanner.nextLine();

                    System.out.println();
                ConsolePrinter.printMenuTitle("────────────────────────────────── Register New User ─ Step 2/4 ──────────────────────────────────");
                    ConsolePrinter.printMenuOption("Please, enter your e-mail address: ");
                    String email = scanner.nextLine();

                    System.out.println();
                ConsolePrinter.printMenuTitle("────────────────────────────────── Register New User ─ Step 3/4 ──────────────────────────────────");
                    ConsolePrinter.printMenuOption("Please, enter your birthday (using this format: dd-mm-yyyy): ");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    LocalDate birthDate = LocalDate.parse(scanner.nextLine(), formatter);

                System.out.println();
                ConsolePrinter.printMenuTitle("────────────────────────────────── Register New User ─ Step 4/4 ──────────────────────────────────");
                ConsolePrinter.printMenuOption("Please, enter your initial investment (minimum 10.000 DKK): ");
                double initialCash = 0;
                while (true) {
                    try {
                        initialCash = Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
                        InitialInvestmentValidator.validate(initialCash);
                        break;
                    } catch (NumberFormatException e) {
                        ConsolePrinter.printError("Please enter a valid number.");
                    } catch (InvalidInputException e) {
                        ConsolePrinter.printError(e.getMessage());
                    }
                }

                int newUserId = userService.getAllUsers().stream()
                            .mapToInt(User::getUserId)
                            .max()
                            .orElse(0) + 1;

                LocalDate createdAt = LocalDate.now();
                User newUser = new User(newUserId, fullName, email, birthDate, initialCash, createdAt, createdAt);

                userService.addUser(newUser);
                System.out.println();
                ConsolePrinter.printMenuTitle("─────────────────────────────────────── Registration Complete ─────────────────────────────────────");
                ConsolePrinter.printConfirmation("Registration complete!");
                ConsolePrinter.printMenuOption("Your unique user ID is " + Colors.ANSI_BLUE + newUserId + Colors.MENUOPTION + " and will be used to log in to your account from now on." +
                        "\nWelcome to Investeringsklubben! ٩(◕‿◕)۶" + Colors.RESET);
                CSVWriter.append(AppConstants.USERS_FILE,newUser);

                start();
                break;
            } else if (choice.equals("2")) {
                start();
                break;
            }
        }
    }
}
