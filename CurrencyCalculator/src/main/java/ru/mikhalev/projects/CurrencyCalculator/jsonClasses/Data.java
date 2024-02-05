package ru.mikhalev.projects.CurrencyCalculator.jsonClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.Map;

/**
 * @author Ivan Mikhalev
 *
 * Класс, использующийся для получения и парсинга ответа от ЦБ
 *
 */

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Data {
    @JsonIgnore
    @JsonProperty("Date")
    private Date Date;
    @JsonIgnore
    @JsonProperty("PreviousDate")
    private Date PreviousDate;
    @JsonIgnore
    @JsonProperty("PreviousURL")
    private String PreviousURL;
    @JsonProperty("Timestamp")
    private String timestamp;
    @JsonProperty("Valute")
    private Map<String, Valute> valute;

}
