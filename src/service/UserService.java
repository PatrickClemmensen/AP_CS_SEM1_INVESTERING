package service;

import model.portfolio.User;
import util.csv.CSVReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UserService {
    // TODO: declare a Map to store users by userId
    private final Map<Integer, User> userMap = new HashMap<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public UserService(String usersFilePath) {
        // TODO: call a private load() method
        load(usersFilePath);
    }

    private void load(String path) {
        // TODO: use CSVReader to read users.csv
        for(String[] row : CSVReader.read(path)){
        // TODO: parse each row into a User object
            int userId = Integer.parseInt(row[0].trim());
            String fullName = row[1].trim();
            String email = row[2].trim();
            LocalDate birthDate = LocalDate.parse(row[3].trim(), FORMATTER);
            double initialCash = Double.parseDouble(row[4].trim().replace(",","."));
            LocalDate createdAt  = LocalDate.parse(row[5].trim(), FORMATTER);
            LocalDate lastUpdated = LocalDate.parse(row[6].trim(), FORMATTER);

            // TODO: put each User into the map using userId as key
            User user = new User(userId, fullName, email, birthDate, initialCash, createdAt, lastUpdated);
            userMap.put(userId,user);
        }
        // Hint: row order is user_id;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated

    }

    public User findById(int userId) {
        // TODO: return the User for the given userId, or null if not found
        return userMap.get(userId);
    }

    public Collection<User> getAllUsers() {
        // TODO: return all users in the map
        return userMap.values();
    }
}