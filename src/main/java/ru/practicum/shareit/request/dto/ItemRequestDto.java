package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

/**
 * DTO of ItemRequest
 * @see ru.practicum.shareit.request.model.ItemRequest
 */
@Data
@SuperBuilder
public class ItemRequestDto {
    private String description;
    private LocalDate created;
}
