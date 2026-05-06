package model.portfolio;

import java.time.LocalDate;

public class User {
    // TODO: declare fields based on users.csv
    // Hint: userId, fullName, email, birthDate, cashBalance, portfolio
    private final int userId;
    private final String fullName;
    private final String email;
    private final LocalDate birthDate;
    private final LocalDate createdAt;
    private final LocalDate lastUpdated;
    private double cashBalance;
    private final Portfolio portfolio;

    public User(int userId, String fullName, String email, LocalDate birthDate,
                double initialCash, LocalDate createdAt, LocalDate lastUpdated) {
        // TODO: initialize fields
        // Hint: create a new Portfolio() here
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.birthDate = birthDate;
        this.cashBalance = initialCash;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
        this.portfolio = new Portfolio();
    }

    // TODO: add getters
    public int getUserId()         { return userId; }
    public String getFullName()    { return fullName; }
    public String getEmail()       { return email; }
    public LocalDate getBirthDate(){ return birthDate; }
    public LocalDate getCreatedAt(){ return createdAt; }
    public LocalDate getLastUpdated(){ return lastUpdated; }
    public double getCashBalance() { return cashBalance; }
    public Portfolio getPortfolio(){ return portfolio; }

    public void deductCash(double amount) {
        // TODO: subtract amount from cashBalance
        cashBalance -= amount;
    }

    public void addCash(double amount) {
        // TODO: add amount to cashBalance
        cashBalance += amount;
    }

    @Override
    public String toString() {
        // TODO: return a readable summary, e.g. "[1] Maria Jensen | Cash: 100000.00 DKK"
        return String.format("[%d] %s | Cash: %.2f DKK", userId, fullName, cashBalance);
    }
}