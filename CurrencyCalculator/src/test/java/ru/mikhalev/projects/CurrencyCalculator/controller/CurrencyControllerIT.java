package ru.mikhalev.projects.CurrencyCalculator.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.mikhalev.projects.CurrencyCalculator.service.CurrencyService;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ivan Mikhalev
 *
 * Класс с интеграционными тестами
 *
 */

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class CurrencyControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    CurrencyService currencyService;

    @Test
    @DisplayName("Возвращает сумму, после конвертации валют")
    void getAmountByArchiveCurrencies_ReturnValidAmount() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders
                .get("/currency/calculate/archive?necessaryDate=2023-07-20&currentCurrency=RUB&necessaryCurrency=USD&amount=100");

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @DisplayName("Возвращает сообщение об ошибке")
    void getAmountByArchiveCurrencies_ReturnResponseMessage() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders
                .get("/currency/calculate/archive?necessaryDate=2023-07-20&currentCurrency=gagga&necessaryCurrency=USD&amount=100");

        //when
        this.mockMvc.perform(requestBuilder)

                // then
                .andExpectAll(status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON));
    }
}
