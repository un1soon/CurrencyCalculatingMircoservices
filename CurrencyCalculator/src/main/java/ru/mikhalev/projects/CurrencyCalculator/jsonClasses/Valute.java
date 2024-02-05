package ru.mikhalev.projects.CurrencyCalculator.jsonClasses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Ivan Mikhalev
 *
 * Класс, содержащий в себе данные о полученных валютах.
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Valute {

    @JsonProperty("ID")
    private String id;
    @JsonProperty("CharCode")
    private String charCode;
    @JsonProperty("Value")
    private BigDecimal value;
    @JsonIgnore
    @JsonProperty("NumCode")
    private int NumCode;
    @JsonIgnore
    @JsonProperty("Nominal")
    private int Nominal;
    @JsonIgnore
    @JsonProperty("Name")
    private String Name;
    @JsonIgnore
    @JsonProperty("Previous")
    private BigDecimal Previous;
}
