package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * DTO of Item
 */
@Data
@SuperBuilder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("available")
    private boolean isAvailable;
    private Long requestId;

}
