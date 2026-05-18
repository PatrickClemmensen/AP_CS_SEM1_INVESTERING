package interfaces;

/**
 * Marks a class as serializable to CSV format.
 * <p>
 *     Classes implementing this interface are responsible for converting
 *     their own fields into a semicolon-delimited string that matches
 *     the column order of their corresponding CSV file.
 * </p>
 */
public interface CSVSerializable {
    /**
     * Converts this object into a semicolon-delimited CSV line.
     *
     * @return a CSV-formatted {@code String} representing this object
     */
    String toCSVLine();
}
