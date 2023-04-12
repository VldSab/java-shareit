package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of UserService.
 *
 * @see UserService
 */
@Service
@RequiredArgsConstructor
public class UserServiceStandard implements UserService {

    @Qualifier("inMemoryUserRepository")
    private final UserRepository userRepository;

    @Override
    public List<UserDto> list() {
        return userRepository.list().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        return UserMapper.toDto(user.get());
    }

    @Override
    public UserDto addUser(User user) {
        if (user.getEmail() == null || user.getName() == null)
            throw new ValidationException("Объект User содержит не все обязательные поля");
        if (userRepository.isEmailExists(user.getEmail()))
            throw new AlreadyExistsException("Пользователь с таким email уже существует");
        return UserMapper.toDto(userRepository.addUser(user));
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        Optional<User> currentUser = userRepository.getUserById(userId);
        if (currentUser.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        if (user.getEmail() != null
                && userRepository.isEmailExists(user.getEmail())
                && !user.getEmail().equals(getUserById(userId).getEmail()))
            throw new AlreadyExistsException("Не удалось обновить. Пользователь с таким email уже существует");
        return UserMapper.toDto(userRepository.updateUser(userId, user));
    }

    @Override
    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.getUserById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        return userRepository.deleteUser(userId);
    }
}
