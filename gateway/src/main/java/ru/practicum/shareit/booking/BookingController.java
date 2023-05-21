package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.constants.Headers;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Object> save(
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestBody @Valid ExternalBookingDto booking
    ) {
        return bookingService.save(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approve(
            @RequestHeader(Headers.USER_ID) Long ownerId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved
    ) {
        return bookingService.approve(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @RequestHeader(Headers.USER_ID) Long userId,
            @PathVariable Long bookingId
    ) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByBooker(
            @RequestHeader(Headers.USER_ID) Long bookerId,
            @RequestParam(defaultValue = "ALL") String state,
            Integer from,
            Integer size
    ) {
        return bookingService.getBookingsByBookerId(bookerId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByOwner(
            @RequestHeader(Headers.USER_ID) Long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            Integer from,
            Integer size
    ) {
        return bookingService.getBookingsByOwner(ownerId, state, from, size);
    }
}
