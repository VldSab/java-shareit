package ru.practicum.shareit.exceptions;

public class ObjectUnavailableException extends RuntimeException {
    public ObjectUnavailableException() {
    }

    public ObjectUnavailableException(String message) {
        super(message);
    }
}
