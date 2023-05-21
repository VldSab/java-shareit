package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * DTO of ItemRequest
 *
 * @see ru.practicum.shareit.request.model.ItemRequest
 */
@Data
@SuperBuilder
public class ItemRequestDto {
    private Long id;
    private String description;
    private LocalDateTime created;
}
