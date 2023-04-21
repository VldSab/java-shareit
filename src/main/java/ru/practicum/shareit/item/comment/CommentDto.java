package ru.practicum.shareit.item.comment;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
