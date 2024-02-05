package ru.mikhalev.projects.CurrencyCalculator.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import ru.mikhalev.projects.CurrencyCalculator.exception.AmountLessThanZeroException;
import ru.mikhalev.projects.CurrencyCalculator.exception.WrongRequestedCurrencyException;
import ru.mikhalev.projects.CurrencyCalculator.util.ResponseMessage;

import java.time.LocalDateTime;

/**
 * @author Ivan Mikhalev
 *
 * Класс для обработки исключений
 *
 */

@ControllerAdvice
@Slf4j
public class CurrencyControllerAdvice {

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> httpClientErrorException(HttpClientErrorException ex) {
        log.error("Курс ЦБ РФ на данную дату не установлен или указана ошибочная дата.");
        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .message("Курс ЦБ РФ на данную дату не установлен или указана ошибочная дата.")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongRequestedCurrencyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> wrongRequestedCurrencyException(WrongRequestedCurrencyException ex) {
        log.error(ex.getMessage());
        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> nullPointerException(NullPointerException ex) {
        log.error("Requested currencies wasn't found in database");
        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .message("Requested currencies wasn't found in database. Check requested char codes")
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AmountLessThanZeroException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseMessage> amountLessThanZeroException(AmountLessThanZeroException ex) {
        log.error(ex.getMessage());
        ResponseMessage responseMessage = ResponseMessage
                .builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
    }
}
