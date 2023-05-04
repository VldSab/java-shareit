package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
public class ItemResponse {
    private final Long id;
    private final Long requestId;
    private final Long itemId;
    private final String name;
    private final Long ownerId;
    private final String description;
    private final Boolean available;
}
