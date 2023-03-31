package ru.practicum.shareit.user.dto;

import ru.practicum.shareit.user.model.User;

/**
 * Mapping User to UserDto
 * @see User
 * @see UserDto
 */
public class UserMapper {
    public UserDto toUserDTO(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
