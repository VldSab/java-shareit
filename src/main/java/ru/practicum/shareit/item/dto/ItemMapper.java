package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

/**
 * Mapping Item to ItemDto
 *
 * @see Item
 * @see ItemDto
 */
public class ItemMapper {
    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getIsAvailable())
                .build();
    }

    public static ItemInfoDto toInfoDto(Item item, List<CommentDto> comments, Booking last, Booking next) {
        return ItemInfoDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .isAvailable(item.getIsAvailable())
                .lastBooking(last != null ? new ItemInfoDto.BookingDto(last.getId(), last.getBooker().getId()) : null)
                .nextBooking(next != null ? new ItemInfoDto.BookingDto(next.getId(), next.getBooker().getId()) : null)
                .comments(comments)
                .build();
    }
}