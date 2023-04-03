package ru.practicum.shareit.item.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.model.User;

/**
 * Item model
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    Long id;
    String name;
    String description;
    @JsonProperty("available")
    boolean isAvailable;
    User owner;
}
