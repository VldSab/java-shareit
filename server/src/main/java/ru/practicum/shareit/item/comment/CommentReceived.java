package ru.practicum.shareit.item.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Comment data transfer object for receiving.
 * @see Comment
 * @see CommentDto
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentReceived {
    @NotBlank
    String text;
}
