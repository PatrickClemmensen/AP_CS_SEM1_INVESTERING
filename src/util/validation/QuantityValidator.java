package util.validation;

public class QuantityValidator {

    public static void validate(int quantity) {
        // TODO: throw IllegalArgumentException if quantity is zero or negative
        if(quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }
    }
}