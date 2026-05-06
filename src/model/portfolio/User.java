package model.portfolio;

import java.time.LocalDate;

public class User {
    // TODO: declare fields based on users.csv
    // Hint: userId, fullName, email, birthDate, cashBalance, portfolio, createdAt, lastUpdated

    public User(int userId, String fullName, String email,
                LocalDate birthDate, double initialCash, LocalDate createdAt, LocalDate lastUpdated) {
        // TODO: initialize fields
        // Hint: create a new Portfolio() here
    }

    // TODO: add getters

    public void deductCash(double amount) {
        // TODO: subtract amount from cashBalance
    }

    public void addCash(double amount) {
        // TODO: add amount to cashBalance
    }

    @Override
    public String toString() {
        // TODO: return a readable summary, e.g. "[1] Maria Jensen | Cash: 100000.00 DKK"
        return null;
    }
}