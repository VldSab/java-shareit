package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * ItemRequest model
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class ItemRequest {
    private Long id;
    @NotBlank
    private String description;
    private User requester;
    private LocalDateTime created = LocalDateTime.now();

}
