package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

/**
 * Mapping User to UserDto
 * @see User
 * @see UserDto
 */
public class UserMapper {
    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
