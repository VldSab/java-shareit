package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class MainExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHappinessOverflow(final NullPointerException e) {
        return Map.of(
                "error", "Не передан параметр",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleHappinessOverflow(final AlreadyExistsException e) {
        return Map.of(
                "error", "Ресурс уже существует",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleHappinessOverflow(final NotFoundException e) {
        return Map.of(
                "error", "Ресурс не найден",
                "message", e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHappinessOverflow(final ValidationException e) {
        return Map.of(
                "error", "Передан невалидный объект",
                "message", e.getMessage()
        );
    }
}
