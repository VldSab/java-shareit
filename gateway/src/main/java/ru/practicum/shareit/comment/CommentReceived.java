package ru.practicum.shareit.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Comment data transfer object for receiving.
 *
 * @see CommentDto
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentReceived {
    @NotBlank
    String text;
}
