package interfaces;

public interface CurrencyConverter {
    // TODO: convert amount from one currency to another using loaded exchange rates
    double convert(double amount, String fromCurrency, String toCurrency);
}