package ru.practicum.shareit.booking;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;

/**
 * Interface for booking business logic.
 */
public interface BookingService {
    ResponseEntity<Object> save(Long userId, ExternalBookingDto booking);

    ResponseEntity<Object> approve(Long ownerId, Long bookingId, boolean approved);

    ResponseEntity<Object> getBookingById(Long userId, Long bookingId);

    ResponseEntity<Object> getBookingsByBookerId(Long bookerId, String state, Integer from, Integer size);

    ResponseEntity<Object> getBookingsByOwner(Long ownerId, String state, Integer from, Integer size);
}
