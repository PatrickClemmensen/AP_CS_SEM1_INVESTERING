package interfaces;

/**
 * Defines a contract for converting monetary amounts between currencies.
 * <p>
 *     Classes implementing this interface are responsible for loading
 *     and applying exchange rates to convert amounts from one currency to another.
 * </p>
 */
public interface CurrencyConverter {
    /**
     * Converts a monetary amount from one currency to another.
     *
     * @param amount        the monetary amount to convert
     * @param fromCurrency  the currency code to convert from (e.g. {@code "USD"})
     * @param toCurrency    the currency code to convert to (e.g. {@code "DKK"})
     * @return the converted amount in the target currency
     */
    double convert(double amount, String fromCurrency, String toCurrency);
}