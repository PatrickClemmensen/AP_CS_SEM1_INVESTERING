package service;

import model.portfolio.User;
import java.util.Collection;

public class UserService {
    // TODO: declare a Map to store users by userId

    public UserService(String usersFilePath) {
        // TODO: call a private load() method
    }

    private void load(String path) {
        // TODO: use CSVReader to read users.csv
        // TODO: parse each row into a User object
        // TODO: put each User into the map using userId as key
        // Hint: row order is user_id;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated
    }

    public User findById(int userId) {
        // TODO: return the User for the given userId, or null if not found
        return null;
    }

    public Collection<User> getAllUsers() {
        // TODO: return all users in the map
        return null;
    }
}