package model.portfolio;

import interfaces.CSVSerializable;
import interfaces.Rankable;
import util.printing.ConsolePrinter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Represents a registered member of the investment club.
 * <p>
 *     A {@code User} holds personal information, a cash balance, and a {@link Portfolio}
 *     of stock positions. The cash balance is updated as the member buys and sells stocks.
 *     The portfolio is loaded lazily from the transactions CSV first time it is needed.
 * </p>
 *
 * @see Portfolio
 */
public class User implements CSVSerializable, Comparable<User>, Rankable {
    // TODO: declare fields based on users.csv
    // Hint: userId, fullName, email, birthDate, cashBalance, portfolio, createdAt, lastUpdated
    private final int userId;
    private final String fullName;
    private final String email;
    private final LocalDate birthDate;
    private final LocalDate createdAt;
    private LocalDate lastUpdated;
    private double cashBalance;
    private double initialCash;
    private final Portfolio portfolio;
    private boolean portfolioLoaded = false;


    /**
     * Constructs a new {@code User} with the given personal details and starting cash balance.
     * <p>
     *     A new empty {@link Portfolio} is created automatically upon construction.
     * </p>
     *
     * @param userId        the unique identifier for this user
     * @param fullName      the user's full name
     * @param email         the user's email address
     * @param birthDate     the user's date of birth
     * @param initialCash   the starting cash balance in DKK
     * @param createdAt     the date this user account was created
     * @param lastUpdated   the date this user's record was last modified
     */
    public User(int userId, String fullName, String email,
                LocalDate birthDate, double initialCash, LocalDate createdAt, LocalDate lastUpdated) {
        // TODO: initialize fields
        // Hint: create a new Portfolio() here
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.birthDate = birthDate;
        this.cashBalance = initialCash;
        this.initialCash = initialCash;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.portfolio = new Portfolio();
    }

    // TODO: add getters
    public int getUserId() {return userId;}
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDate getBirthDate() { return birthDate; }
    public LocalDate getCreatedAt() { return createdAt; }
    public LocalDate getLastUpdated() { return lastUpdated; }
    public double getCashBalance() { return cashBalance; }
    public Portfolio getPortfolio() { return portfolio; }
    public boolean isPortfolioLoaded() { return portfolioLoaded; }
    public void setPortfolioLoaded(boolean loaded) { this.portfolioLoaded = loaded; }

    public double getCash(){
        return cashBalance;
    }
    public double getInitialCash() { return initialCash; }

    /**
     * Deducts the given amount from the user's cash balance.
     * <p>
     *     Called when the user purchase stocks. No bounds check is performed here -
     *     callers are responsible for validating sufficient funds before invoking this method.
     * </p>
     *
     * @param amount the amount to deduct in DKK
     */
    public void deductCash(double amount) {
        // TODO: subtract amount from cashBalance
        cashBalance -= amount;
    }

    /**
     * Adds the given amount to the user's cash balance
     * <p>
     *     Called when the user sells stocks.
     * </p>
     *
     * @param amount the amount to add in DKK
     */
    public void addCash(double amount) {
        // TODO: add amount to cashBalance
        cashBalance += amount;
    }


    /**
     * Returns a short, human-readable summary of this user.
     * <p>
     *     Format: {@code [userId] fullName | Cash: cashBalance DKK}
     * </p>
     *
     * @return a formatted summary string
     */
    @Override
    public String toString() {
        // TODO: return a readable summary, e.g. "[1] Maria Jensen | Cash: 100000.00 DKK"
        return String.format("[%d] %s | Cash: %.2f DKK", userId, fullName, cashBalance);
    }

    /**
     * Serializes this user to a semicolon-delimited CSV line.
     * <p>
     *     Format: {@code userId;fullName;email;birthDate;cashBalance;createdAt;lastUpdated
     *     Dates are formatted as {@code dd-MM-yyyy}}
     * </p>
     *
     * @return a CSV-formatted {@code String} representing this user
     */
    @Override
    public String toCSVLine() {
       // userId, fullName, email, birthDate, cashBalance, portfolio, createdAt, lastUpdated
        return String.join(";",
                String.valueOf(userId),
                String.valueOf(fullName),
                String.valueOf(email),
                birthDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                String.valueOf(cashBalance),
                createdAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                lastUpdated.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    @Override
    public int compareTo(User other) {
        return Double.compare(other.getRankValue(), this.getRankValue()); // descending — highest value first
    }

    @Override
    public double getRankValue() {
        return cashBalance + portfolio.getTotalValue();
    }
}