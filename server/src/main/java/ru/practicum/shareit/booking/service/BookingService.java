package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;

import java.util.List;

/**
 * Interface for booking business logic.
 *
 * @see ru.practicum.shareit.booking.Booking
 * @see ru.practicum.shareit.booking.controller.BookingController
 */
public interface BookingService {
    BookingDto save(Long userId, ExternalBookingDto booking);

    BookingDto approve(Long ownerId, Long bookingId, boolean approved);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookingsByBookerId(Long bookerId, String state, Integer from, Integer size);

    List<BookingDto> getBookingsByOwner(Long ownerId, String state, Integer from, Integer size);
}
