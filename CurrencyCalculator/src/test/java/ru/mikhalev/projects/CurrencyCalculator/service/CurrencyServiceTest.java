package ru.mikhalev.projects.CurrencyCalculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import ru.mikhalev.projects.CurrencyCalculator.entity.Currency;
import ru.mikhalev.projects.CurrencyCalculator.jsonClasses.Data;
import ru.mikhalev.projects.CurrencyCalculator.jsonClasses.Valute;
import ru.mikhalev.projects.CurrencyCalculator.repository.CurrencyRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Mikhalev
 */


@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {

    String archiveURL = "https://www.cbr-xml-daily.ru//archive//%s//%s/%s//daily_json.js";

    @Mock
    RestTemplate restTemplate = new RestTemplate();

    @Mock
    CurrencyRepository currencyRepository;

    @InjectMocks
    CurrencyService currencyService;

    @Test
    @DisplayName("Рассчет суммы на основе архивных курсов валют по указанной дате")
    void getAmountByArchiveCurrencies_ReturnValidAmount() throws JsonProcessingException {
        //given
        ReflectionTestUtils.setField(currencyService, "archiveURL", "https://www.cbr-xml-daily.ru//archive//%s//%s/%s//daily_json.js");
        LocalDate date = LocalDate.of(2023, Month.JULY, 20);
        String currentCurrency = "USD";
        String necessaryCurrency = "RUB";
        BigDecimal amount = BigDecimal.valueOf(100);

        //when
        BigDecimal result = currencyService.getAmount(date, currentCurrency, necessaryCurrency, amount);

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(9120.4600, result.doubleValue());
    }

    @Test
    @DisplayName("Рассчет суммы на основе текущих курсов валют")
    void getAmountByCurrentCurrencies_ReturnValidAmount() {
        //given
        ReflectionTestUtils.setField(currencyService, "archiveURL", "https://www.cbr-xml-daily.ru//archive//%s//%s/%s//daily_json.js");
        String currentCurrency = "USD";
        String necessaryCurrency = "RUB";
        BigDecimal amount = BigDecimal.valueOf(100);
        Currency currency = Currency
                .builder()
                .id("352kfs")
                .charCode("TYE")
                .value(BigDecimal.valueOf(34))
                .build();

        Mockito.doReturn(currency).when(this.currencyRepository).getCurrencyByCharCode(currentCurrency);

        //when
        BigDecimal result = currencyService.getAmount(currentCurrency, necessaryCurrency, amount);


        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(3400, result.doubleValue());
    }

    @Test
    @DisplayName("Получение от ЦБ строки со всеми данными о валютах")
    void sendRequestToTheCentralBank_ReturnJsonStringWithAllDataAboutCurrencies() {
        //given
        String URL = "https://www.cbr-xml-daily.ru//archive//2023//07/20//daily_json.js";

        //when
        String jsonString = this.currencyService.sendRequestToTheCentralBank(URL);

        //then
        Assertions.assertNotNull(jsonString);
    }

    @Test
    @DisplayName("Преобразование строки, полученной от ЦБ в объект класса Data")
    void mapReceivedData_ReturnObjectData() throws JsonProcessingException {
        //given
        String URL = "https://www.cbr-xml-daily.ru//archive//2023//07/20//daily_json.js";
        String jsonString = this.currencyService.sendRequestToTheCentralBank(URL);

        //when
        Data data = this.currencyService.mapReceivedData(jsonString);

        //then
        Assertions.assertNotNull(data);
        Assertions.assertEquals(43, data.getValute().size());
    }

    @Test
    @DisplayName("Получение мапы с валютами")
    void getAllArchiveCurrencies_ReturnMapWithCurrencies() throws JsonProcessingException {
        //given
        ReflectionTestUtils.setField(currencyService, "archiveURL", "https://www.cbr-xml-daily.ru//archive//%s//%s/%s//daily_json.js");
        LocalDate date = LocalDate.of(2023, Month.JULY, 20);

        //when
        Map<String, Valute> valutes = this.currencyService.getAllArchiveCurrencies(date);

        //then
        Assertions.assertNotNull(valutes);
        Assertions.assertEquals(43, valutes.size());
        Assertions.assertEquals("USD", valutes.values().stream().filter(x -> x.getId().equals("R01235")).findAny().get().getCharCode());
    }
}
