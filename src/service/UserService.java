package service;

import model.portfolio.User;
import util.csv.CSVReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsible for loading and querying user data
 * <p>
 *     Users are read from a CSV file at startup and stored in memory,
 *     indexed by their unique ID for fast lookup
 * </p>
 */
public class UserService {
    // TODO: declare a Map to store users by userId
    private final Map<Integer, User> userMap = new HashMap<>();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Constructs a new {@code UserService} and load user data from the given CSV file
     *
     * @param usersFilePath the path to the CSV file containing user data
     */
    public UserService(String usersFilePath) {
        // TODO: call a private load() method
        load(usersFilePath);
    }

    /**
     * Reads and parses user data from the specified CSV file into {@link #userMap}
     * <p>
     *     Each row in the CSV file is expected to follow this column order:
     *     {@code user_id;full_name;email;birth_date;initial_cash_DKK;created_at;last_updated}
     * </p>
     * <p>
     *     Decimal commas are normalized to decimal points before parsing.
     * </p>
     * @param path the file path to the CSV file to load
     */
    private void load(String path) {
        // TODO: use CSVReader to read users.csv
        for(String[] row : CSVReader.read(path)){
        // TODO: parse each row into a User object
            int userId = Integer.parseInt(row[0].trim());
            String fullName = row[1].trim();
            String email = row[2].trim();
            LocalDate birthDate = LocalDate.parse(row[3].trim(), FORMATTER);
            double initialCash 