package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.comment.CommentDto;

import java.util.List;

@Data
@AllArgsConstructor
@SuperBuilder
public class ItemInfoDto {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("available")
    private boolean isAvailable;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;
    private Long requestId;

    @Data
    @AllArgsConstructor
    public static class BookingDto {
        private Long id;
        private Long bookerId;
    }
}
