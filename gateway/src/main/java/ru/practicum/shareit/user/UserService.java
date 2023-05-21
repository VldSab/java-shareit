package ru.practicum.shareit.user;

import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<Object> list();

    ResponseEntity<Object> getUserById(Long userId);

    ResponseEntity<Object> addUser(User user);

    ResponseEntity<Object> updateUser(Long id, User user);

    ResponseEntity<Object> deleteUser(Long userId);

}
