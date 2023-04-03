package ru.practicum.shareit.user.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * DTO of User
 * @see ru.practicum.shareit.user.model.User
 */
@Data
@SuperBuilder
public class UserDto {
    private Long id;
    private String name;
    private String email;
}
