package ru.mikhalev.projects.CurrencyCalculator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.mikhalev.projects.CurrencyCalculator.exception.AmountLessThanZeroException;
import ru.mikhalev.projects.CurrencyCalculator.service.CurrencyService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * @author Ivan Mikhalev
 *
 * Класс для тестирования класса CurrencyController
 *
 */

@ExtendWith(MockitoExtension.class)
public class CurrencyControllerTest {

    @Mock
    CurrencyService currencyService;

    @InjectMocks
    CurrencyController currencyController;

    @Test
    @DisplayName("GET /calculate/archive возвращает сумму, полученную в ходе конвертации указанных валют на указанную дату")
    void getAmountByArchiveCurrencies_ReturnValidAmount() throws JsonProcessingException {
        // given
        LocalDate date = LocalDate.of(2023, Month.JULY, 20);
        String currentCurrency = "USD";
        String necessaryCurrency = "RUB";
        BigDecimal amount = BigDecimal.valueOf(100);

        doReturn(BigDecimal.valueOf(9120.4600))
                .when(this.currencyService).getAmount(date, currentCurrency, necessaryCurrency, amount);
        // when
        var responseEntity = this.currencyController
                .getAmountByArchiveCurrencies(date, currentCurrency, necessaryCurrency, amount);
        // then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(9120.4600 ,responseEntity.doubleValue());
        verify(this.currencyService).getAmount(date, currentCurrency, necessaryCurrency, amount);
    }

    @Test
    @DisplayName("GET /calculate возвращает сумму, полученную в ходе конвертации указанных валют на текущую дату")
    void getAmountByCurrentCurrencies_ReturnValidAmount() {
        // given
        String currentCurrency = "USD";
        String necessaryCurrency = "RUB";
        BigDecimal amount = BigDecimal.valueOf(100);

        doReturn(BigDecimal.valueOf(9085.4500))
                .when(this.currencyService).getAmount(currentCurrency, necessaryCurrency, amount);
        // when
        var responseEntity = this.currencyController
                .getAmountByCurrentCurrencies(currentCurrency, necessaryCurrency, amount);
        // then
        Assertions.assertNotNull(responseEntity);
        Assertions.assertEquals(9085.4500 ,responseEntity.doubleValue());
        verify(this.currencyService).getAmount(currentCurrency, necessaryCurrency, amount);
    }

    @Test
    @DisplayName("Запрошенное количество валюты не меньше 0")
    void checkAmountInRequest_CheckAmountInRequest() {
        // given
        BigDecimal amount =  BigDecimal.valueOf(-100);

        // when
        Throwable thrown = assertThrows(AmountLessThanZeroException.class, () ->
            this.currencyController.checkAmountInRequest(amount));

        // then
        Assertions.assertNotNull(thrown.getMessage());
        Assertions.assertEquals("Amount in request can't be less than 0", thrown.getMessage());
    }
}
