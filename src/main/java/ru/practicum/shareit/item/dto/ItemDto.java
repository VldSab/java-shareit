package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * DTO of Item
 * @see ru.practicum.shareit.item.model.Item
 */
@Data
@SuperBuilder
public class ItemDto {
    String name;
    String description;
    @JsonProperty("available")
    boolean isAvailable;

}
