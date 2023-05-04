package ru.practicum.shareit.item.comment;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Comment data transfer object.
 * @see Comment
 */
@Data
@SuperBuilder
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
