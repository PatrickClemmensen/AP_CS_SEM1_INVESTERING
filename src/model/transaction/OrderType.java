package model.transaction;

/**
 * Represents the type of stock market order.
 */
public enum OrderType {
    BUY, SELL;

    /**
     * Converts a string to its corresponding {@code OrderType}.
     *
     * @param value the string to convert (case-insensitive)
     * @return the matching {@code OrderType}
     * @throws IllegalArgumentException if the value does not match any order type
     */
    public static OrderType fromString(String value){
        return OrderType.valueOf(value.trim().toUpperCase());
    }
}
