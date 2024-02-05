package ru.mikhalev.projects.CurrencyCalculator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mikhalev.projects.CurrencyCalculator.exception.AmountLessThanZeroException;
import ru.mikhalev.projects.CurrencyCalculator.service.CurrencyService;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Ivan Mikhalev
 *
 * Контроллер, принимающий запросы на калькулятор валют
 *
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/currency")
public class CurrencyController {

    private final CurrencyService currencyService;

    @GetMapping("/calculate")
    @Schema(name = "Обмен валют по текущему курсу")
    public BigDecimal getAmountByCurrentCurrencies(
            @RequestParam(value = "currentCurrency")
            @Schema(example = "USD")
            String currentCurrency,
            @Schema(example = "RUB")
            @RequestParam("necessaryCurrency")
            String necessaryCurrency,
            @Schema(example = "10", implementation = BigDecimal.class)
            @RequestParam(value = "amount")
            BigDecimal amount) {

        checkAmountInRequest(amount);

        return currencyService.getAmount(currentCurrency, necessaryCurrency, amount);
    }

    @GetMapping("/calculate/archive")
    @Schema(name = "Обмен валют по архивному курсу")
    public BigDecimal getAmountByArchiveCurrencies(
            @Schema(example = "2023-07-20")
            @RequestParam(value = "necessaryDate")
            LocalDate necessaryDate,
            @Schema(example = "USD")
            @RequestParam(value = "currentCurrency")
            String currentCurrency,
            @Schema(example = "RUB")
            @RequestParam("necessaryCurrency")
            String necessaryCurrency,
            @Schema(example = "10", implementation = BigDecimal.class)
            @RequestParam(value = "amount")
            BigDecimal amount) throws JsonProcessingException {

        checkAmountInRequest(amount);

        return currencyService.getAmount(necessaryDate, currentCurrency, necessaryCurrency, amount);
    }

    public void checkAmountInRequest(BigDecimal amount) {
        if(amount.doubleValue() < 0) {
            throw new AmountLessThanZeroException();
        }
    }
}
