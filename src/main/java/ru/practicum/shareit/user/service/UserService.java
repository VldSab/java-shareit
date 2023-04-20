package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> list();

    UserDto getUserById(Long userId);

    UserDto addUser(User user);

    UserDto updateUser(Long id, User user);

    boolean deleteUser(Long userId);

}
