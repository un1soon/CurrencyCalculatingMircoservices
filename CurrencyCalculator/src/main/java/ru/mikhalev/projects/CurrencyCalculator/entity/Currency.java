package ru.mikhalev.projects.CurrencyCalculator.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

/**
 * @author Ivan Mikhalev
 *
 * Класс, хранящий в себе данный о последнем курсе валют. Все валюты находятся в базе данных.
 *
 */

@Entity
@Table(name = "currency")
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Currency {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "char_code")
    private String charCode;
    @Column(name = "value")
    private BigDecimal value;
}
