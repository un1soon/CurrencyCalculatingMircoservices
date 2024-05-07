package ru.mikhalev.projects.CurrencyCalculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.mikhalev.projects.CurrencyCalculator.entity.Currency;
import ru.mikhalev.projects.CurrencyCalculator.exception.WrongRequestedCurrencyException;
import ru.mikhalev.projects.CurrencyCalculator.jsonClasses.Data;
import ru.mikhalev.projects.CurrencyCalculator.jsonClasses.Valute;
import ru.mikhalev.projects.CurrencyCalculator.mapper.CurrencyMapper;
import ru.mikhalev.projects.CurrencyCalculator.repository.CurrencyRepository;
import ru.mikhalev.projects.CurrencyCalculator.scheduler.CentralBankRequestTask;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;


/**
 * @author Ivan Mikhalev
 *
 * Сервис для рассчета курсов валют
 *
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class CurrencyService {
    @Value("${url.to.central.bank.archive}")
    private String archiveURL;

    @Value("${url.to.central.bank.now}")
    private String URL;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CurrencyRepository currencyRepository;

    public BigDecimal getAmount(String currentCurrency, String necessaryCurrency, BigDecimal amount) throws JsonProcessingException {

        if(currencyRepository.findAll().size() == 0) {
            sendRequestToTheCentralBankInFirstStartApp();
        }

        if(currentCurrency.equals("RUB")) {
            BigDecimal necessaryCurrencyFromDatabase = currencyRepository.getCurrencyByCharCode(necessaryCurrency).getValue();
            return amount.divide(necessaryCurrencyFromDatabase, 2, RoundingMode.HALF_UP);
        } else if (necessaryCurrency.equals("RUB")) {
            BigDecimal currentCurrencyFromDatabase = currencyRepository.getCurrencyByCharCode(currentCurrency).getValue();
            return currentCurrencyFromDatabase.multiply(amount);
        }
        BigDecimal currentCurrencyValueFromDatabase = currencyRepository.getCurrencyByCharCode(currentCurrency).getValue();
        BigDecimal necessaryCurrencyValueFromDatabase = currencyRepository.getCurrencyByCharCode(necessaryCurrency).getValue();

        return currentCurrencyValueFromDatabase.multiply(amount).divide(necessaryCurrencyValueFromDatabase, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAmount(LocalDate necessaryDate, String currentCurrency, String necessaryCurrency, BigDecimal amount) throws JsonProcessingException {
        Map<String, Valute> archiveCurrencies = getAllArchiveCurrencies(necessaryDate);
        if(!validateRequestedCurrencies(currentCurrency, necessaryCurrency, archiveCurrencies)) {
            throw new WrongRequestedCurrencyException();
        }

        BigDecimal necessaryCurrencyFromArchive = new BigDecimal(0);
        BigDecimal currentCurrencyFromArchive = new BigDecimal(0);
            while (necessaryCurrencyFromArchive.equals(BigDecimal.ZERO) & currentCurrencyFromArchive.equals(BigDecimal.ZERO)) {
                for(Valute valute : archiveCurrencies.values()) {
                    if (currentCurrency.equals("RUB")) {
                        if(valute.getCharCode().equals(necessaryCurrency)) {
                            return amount.divide(valute.getValue(),2,RoundingMode.HALF_UP);
                        }
                    } else if(necessaryCurrency.equals("RUB")) {
                        if(valute.getCharCode().equals(currentCurrency)) {
                            return valute.getValue().multiply(amount);
                        }
                    }

                    if(valute.getCharCode().equals(necessaryCurrency)) {
                        necessaryCurrencyFromArchive = valute.getValue();
                    }
                    if(valute.getCharCode().equals(currentCurrency)) {
                        currentCurrencyFromArchive = valute.getValue();
                    }
                }
            }
            return currentCurrencyFromArchive.multiply(amount).divide(necessaryCurrencyFromArchive, 2, RoundingMode.HALF_UP);
    }

    public boolean validateRequestedCurrencies(String currentCurrency, String necessaryCurrency, Map<String, Valute> archiveCurrencies) {
        boolean currentCurrencyIsFound = false;
        boolean necessaryCurrencyIsFound = false;

        if(currentCurrency.equals("RUB"))
            currentCurrencyIsFound = true;

        if(necessaryCurrency.equals("RUB"))
            necessaryCurrencyIsFound = true;

        for(Valute valute : archiveCurrencies.values()) {
            if(currentCurrencyIsFound) {
                if(valute.getCharCode().equals(necessaryCurrency)) {
                    necessaryCurrencyIsFound = true;
                }
            } else if(necessaryCurrencyIsFound) {
                if(valute.getCharCode().equals(currentCurrency)) {
                    currentCurrencyIsFound = true;
                }
            } else {
                if(valute.getCharCode().equals(currentCurrency))
                    currentCurrencyIsFound = true;

                if(valute.getCharCode().equals(necessaryCurrency))
                    necessaryCurrencyIsFound = true;
            }
        }

        return currentCurrencyIsFound & necessaryCurrencyIsFound;
    }

    public Map<String ,Valute> getAllArchiveCurrencies(LocalDate necessaryDate) throws JsonProcessingException {
        if(necessaryDate.getMonthValue() < 10) {
            String jsonString = sendRequestToTheCentralBank(String.format(archiveURL, necessaryDate.getYear(), "0" + necessaryDate.getMonthValue(), necessaryDate.getDayOfMonth()));
            return mapReceivedData(jsonString).getValute();
        }
        String jsonString = sendRequestToTheCentralBank(String.format(archiveURL, necessaryDate.getYear(), necessaryDate.getMonthValue(), necessaryDate.getDayOfMonth()));
        return mapReceivedData(jsonString).getValute();
    }

    public String sendRequestToTheCentralBank(String URLToTheCentralBank) {
        return restTemplate.getForObject(URLToTheCentralBank, String.class);
    }


    public Data mapReceivedData(String jsonString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper.readValue(jsonString, Data.class);
    }

    public void sendRequestToTheCentralBankInFirstStartApp() throws JsonProcessingException {
        log.info("A request to the central bank has been sent in: " + LocalDate.now());
        String jsonString = sendRequestToTheCentralBank(URL);
        Map<String, Valute> mapWithValutes = mapReceivedDataInValutes(jsonString);
        updateCurrenciesInDatabase(mapWithValutes);
    }

    public Map<String, Valute> mapReceivedDataInValutes(String jsonString) throws JsonProcessingException {
        Data mappedData = mapReceivedData(jsonString);
        return mappedData.getValute();
    }

    public void updateCurrenciesInDatabase(Map<String, Valute> mapWithValutes) {
        for(Valute valute : mapWithValutes.values()) {
            log.info(String.valueOf(valute));
            currencyRepository.save((CurrencyMapper.INSTANCE.toCurrency(valute)));
        }
        log.info("All currencies successfully updated in database");
    }
}
