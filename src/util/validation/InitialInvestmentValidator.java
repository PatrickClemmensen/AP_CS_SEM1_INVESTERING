package util.validation;


import util.exception.InvalidInputException;

public class InitialInvestmentValidator {
    public static void validate(double initialCash) {
        if(initialCash < 10000) {
            throw new InvalidInputException("Initialcash should minimum be 10.000 DKK");
        }
    }
}
