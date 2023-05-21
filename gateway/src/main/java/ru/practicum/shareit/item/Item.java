package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.shareit.user.User;


/**
 * Item model.
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    @JsonProperty("available")
    private Boolean isAvailable;
    private User owner;
    // если вещь добавляется владельцем в ответ на запрос
    // то это поле будет заполненно id-запроса
    private Long requestId;
}
