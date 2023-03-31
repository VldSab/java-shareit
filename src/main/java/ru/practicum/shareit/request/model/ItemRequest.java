package ru.practicum.shareit.request.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;

/**
 * ItemRequest model
 */
@Data
@SuperBuilder
public class ItemRequest {
    private Long id;
    private String description;
    private User requestor;
    private LocalDate created;

}
