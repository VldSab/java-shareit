package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getUserById(Long userId);

    List<User> list();

    User addUser(User user);

    User updateUser(Long id, User user);

    boolean deleteUser(Long userId);

    boolean isEmailExists(String email);

}
