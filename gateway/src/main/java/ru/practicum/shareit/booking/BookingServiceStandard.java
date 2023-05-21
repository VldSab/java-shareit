package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;


/**
 * Implementation of BookingService interface.
 *
 * @see BookingService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceStandard implements BookingService {

    private final BookingWebClient bookingWebClient;

    @Override
    public ResponseEntity<Object> save(Long userId, ExternalBookingDto booking) {
        return bookingWebClient.save(userId, booking);
    }

    @Override
    public ResponseEntity<Object> approve(Long ownerId, Long bookingId, boolean approved) {
        return bookingWebClient.approve(ownerId, bookingId, approved);
    }

    @Override
    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        return bookingWebClient.getBookingById(userId, bookingId);
    }

    @Override
    public ResponseEntity<Object> getBookingsByBookerId(Long bookerId, String state, Integer from, Integer size) {
        return bookingWebClient.getBookingsByBooker(bookerId, state, from, size);
    }

    @Override
    public ResponseEntity<Object> getBookingsByOwner(Long ownerId, String state, Integer from, Integer size) {
        return bookingWebClient.getBookingsByOwner(ownerId, state, from, size);
    }

}
