package ru.practicum.shareit.booking;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.constants.Headers;

import javax.validation.Valid;

@FeignClient(value = "booking", url = "${shareit-server.url}/bookings", decode404 = true)
public interface BookingWebClient {

    @PostMapping
    ResponseEntity<Object> save(
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestBody @Valid ExternalBookingDto booking
    );

    @PatchMapping("/{bookingId}")
    ResponseEntity<Object> approve(
            @RequestHeader(Headers.USER_ID) Long ownerId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved
    );

    @GetMapping("/{bookingId}")
    ResponseEntity<Object> getBookingById(
            @RequestHeader(Headers.USER_ID) Long userId,
            @PathVariable Long bookingId
    );

    @GetMapping
    ResponseEntity<Object> getBookingsByBooker(
            @RequestHeader(Headers.USER_ID) Long bookerId,
            @RequestParam(defaultValue = "ALL") String state,
            @RequestParam Integer from,
            @RequestParam Integer size
    );

    @GetMapping("/owner")
    ResponseEntity<Object> getBookingsByOwner(
            @RequestHeader(Headers.USER_ID) Long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam Integer from,
            @RequestParam Integer size
    );
}
