package ru.mikhalev.projects.CurrencyCalculator.exception;

/**
 * @author Ivan Mikhalev
 *
 * Исключение, возникающее в том случае, когда количество валюты, указанное в запросе меньше нуля.
 *
 */

public class AmountLessThanZeroException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Amount in request can't be less than 0";
    }
}
