package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.ExternalBookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Mock
    private BookingService bookingService;
    @InjectMocks
    private BookingController bookingController;

    private static final User myUser = User.builder()
            .id(1L)
            .email("user@gmail.com")
            .name("John")
            .build();

    private static final User otherUser = User.builder()
            .id(2L)
            .email("other@gmail.com")
            .name("Frank")
            .build();

    private static final Item item = Item.builder()
            .id(1L)
            .name("Item")
            .owner(myUser)
            .description("Simple item")
            .isAvailable(true)
            .build();

    private static final Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now().plusDays(2))
            .end(LocalDateTime.now().plusDays(3))
            .item(item)
            .booker(otherUser)
            .status(BookingStatus.WAITING)
            .build();

    @Test
    void save_whenInvoked_thenReturnBookingDto() {
        ExternalBookingDto externalBooking = ExternalBookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItem().getId())
                .bookerId(booking.getBooker().getId())
                .status(booking.getStatus().name())
                .build();
        BookingDto bookingDto = BookingMapper.toDto(booking);
        when(bookingService.save(myUser.getId(), externalBooking)).thenReturn(bookingDto);

        assertEquals(bookingDto, bookingController.save(myUser.getId(), externalBooking));
    }

    @Test
    void approve_whenInvoked_thenReturnBookingDto() {
        BookingDto bookingDto = BookingMapper.toDto(booking);
        when(bookingService.approve(myUser.getId(), booking.getId(), true))
                .thenReturn(bookingDto);

        assertEquals(bookingDto, bookingController.approve(myUser.getId(), booking.getId(), true));
    }

    @Test
    void getBookingById_whenInvoked_thenReturnBookingDto() {
        BookingDto bookingDto = BookingMapper.toDto(booking);
        when(bookingService.getBookingById(myUser.getId(), booking.getId()))
                .thenReturn(bookingDto);

        assertEquals(bookingDto, bookingController.getBookingById(myUser.getId(), booking.getId()));
    }

    @Test
    void getBookingsByBooker_whenInvoked_thenReturnListOfBookingDto() {
        List<BookingDto> bookingDtoList = List.of(BookingMapper.toDto(booking));
        String state = "ALL";
        when(bookingService.getBookingsByBookerId(otherUser.getId(), state, null, null))
                .thenReturn(bookingDtoList);

        assertEquals(bookingDtoList,
                bookingController.getBookingsByBooker(otherUser.getId(), state, null, null));
    }

    @Test
    void getBookingsByOwner_whenInvoked_thenReturnListOfBookingDto() {
        List<BookingDto> bookingDtoList = List.of(BookingMapper.toDto(booking));
        String state = "ALL";
        when(bookingService.getBookingsByOwner(myUser.getId(), state, null, null))
                .thenReturn(bookingDtoList);

        assertEquals(bookingDtoList,
                bookingController.getBookingsByOwner(myUser.getId(), state, null, null));
    }
}