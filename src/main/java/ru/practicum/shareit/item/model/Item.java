package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.model.User;

/**
 * Item model
 */
@Data
@SuperBuilder
public class Item {
    Long id;
    String name;
    String description;
    @JsonProperty("available")
    boolean isAvailable;
    User owner;
}
