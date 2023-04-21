package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.constants.Headers;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto save(
            @RequestHeader(Headers.USER_ID) Long userId,
            @RequestBody @Valid ExternalBookingDto booking) {
        return bookingService.save(userId, booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(
            @RequestHeader(Headers.USER_ID) Long ownerId,
            @PathVariable Long bookingId,
            @RequestParam boolean approved) {
        return bookingService.approve(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(
            @RequestHeader(Headers.USER_ID) Long userId,
            @PathVariable Long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByBooker(
            @RequestHeader(Headers.USER_ID) Long bookerId,
            @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getBookingsByBookerId(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingsByOwner(
            @RequestHeader(Headers.USER_ID) Long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getBookingsByOwner(ownerId, state);
    }
}