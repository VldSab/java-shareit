package ru.practicum.shareit.comment;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Comment data transfer object.
 */
@Data
@SuperBuilder
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
