package ru.mikhalev.projects.CurrencyCalculator.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.mikhalev.projects.CurrencyCalculator.jsonClasses.Data;
import ru.mikhalev.projects.CurrencyCalculator.jsonClasses.Valute;
import ru.mikhalev.projects.CurrencyCalculator.mapper.CurrencyMapper;
import ru.mikhalev.projects.CurrencyCalculator.repository.CurrencyRepository;
import ru.mikhalev.projects.CurrencyCalculator.service.CurrencyService;

import java.time.LocalDate;
import java.util.Map;


/**
 * @author Ivan Mikhalev
 *
 * Планировщик для обновления курсов валют в базе данных. Отправляет запрос к ЦБ каждый день в 12:00 по МСК
 *
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class CentralBankRequestTask {
    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;

    @Value("${url.to.central.bank.now}")
    private String URL;

    @Scheduled(cron = "0 0 12 * * ?", zone = "Europe/Moscow")
    public void sendRequestToTheCentralBank() throws JsonProcessingException {
        log.info("A request to the central bank has been sent in: " + LocalDate.now());
        String jsonString = currencyService.sendRequestToTheCentralBank(URL);
        Map<String, Valute> mapWithValutes = mapReceivedData(jsonString);
        updateCurrenciesInDatabase(mapWithValutes);
    }

    public Map<String, Valute> mapReceivedData(String jsonString) throws JsonProcessingException {
        Data mappedData = currencyService.mapReceivedData(jsonString);
        return mappedData.getValute();
    }

    public void updateCurrenciesInDatabase(Map<String, Valute> mapWithValutes) {
        for(Valute valute : mapWithValutes.values()) {
            currencyRepository.save((CurrencyMapper.INSTANCE.toCurrency(valute)));
        }

        log.info("All currencies successfully updated in database");
    }
}
