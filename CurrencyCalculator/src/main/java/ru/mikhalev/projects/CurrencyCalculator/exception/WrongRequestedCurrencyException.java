package ru.mikhalev.projects.CurrencyCalculator.exception;

/**
 * @author Ivan Mikhalev
 *
 * Исключение, возникающее при неверно указанных валютах в запросе
 *
 */

public class WrongRequestedCurrencyException extends RuntimeException {
    @Override
    public String getMessage() {
        return "Error in requested currencies";
    }
}
