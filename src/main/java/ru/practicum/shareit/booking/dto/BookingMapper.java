package ru.practicum.shareit.booking.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.dto.UserMapper;

/**
 * Mapper Booking to BookingDto.
 * @see Booking
 * @see BookingDto
 */
public class BookingMapper {

    public static BookingDto toDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .booker(UserMapper.toDto(booking.getBooker()))
                .item(ItemMapper.toDto(booking.getItem()))
                .status(booking.getStatus())
                .build();
    }
}
