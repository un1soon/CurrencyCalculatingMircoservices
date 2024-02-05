package ru.mikhalev.projects.CurrencyCalculator.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author Ivan Mikhalev
 *
 * Класс для отправки ответа пользователю в случае каких-либо ошибок в программе
 *
 */

@Data
@ToString
@Builder
@AllArgsConstructor
public class ResponseMessage {
    private String message;
    private LocalDateTime timestamp;
}
