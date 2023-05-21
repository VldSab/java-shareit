package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService.
 *
 * @see UserService
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceStandard implements UserService {

    private final UserWebClient userWebClient;

    @Override
    public ResponseEntity<Object> list() {
        return userWebClient.listUsers();
    }

    @Override
    public ResponseEntity<Object> getUserById(Long userId) {
        return userWebClient.getUser(userId);
    }

    @Override
    public ResponseEntity<Object> addUser(User user) {
        return userWebClient.addUser(user);
    }

    @Override
    public ResponseEntity<Object> updateUser(Long userId, User user) {
        return userWebClient.updateUser(userId, user);
    }

    @Override
    public ResponseEntity<Object> deleteUser(Long userId) {
        return userWebClient.deleteUser(userId);
    }
}
