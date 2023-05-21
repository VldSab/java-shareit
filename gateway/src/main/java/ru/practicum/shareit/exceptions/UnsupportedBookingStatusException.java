package ru.practicum.shareit.exceptions;

public class UnsupportedBookingStatusException extends RuntimeException {
    public UnsupportedBookingStatusException() {
    }

    public UnsupportedBookingStatusException(String message) {
        super(message);
    }
}
