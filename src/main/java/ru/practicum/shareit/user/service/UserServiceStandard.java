package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.repository.DBUserRepository;
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
@Transactional
public class UserServiceStandard implements UserService {

    private final DBUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> list() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        return UserMapper.toDto(user.get());
    }

    @Override
    public UserDto addUser(User user) {
        if (user.getEmail() == null || user.getName() == null)
            throw new ValidationException("Объект User содержит не все обязательные поля");
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(Long userId, User user) {
        Optional<User> currentUser = userRepository.findById(userId);
        if (currentUser.isEmpty())
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        if (user.getEmail() != null
                && userRepository.existsByEmail(user.getEmail())
                && !user.getEmail().equals(getUserById(userId).getEmail()))
            throw new AlreadyExistsException("Не удалось обновить. Пользователь с таким email уже существует");
        User cur = currentUser.get();
        if (user.getName() != null)
            cur.setName(user.getName());
        if (user.getEmail() != null) {
            cur.setEmail(user.getEmail());
        }
        return UserMapper.toDto(userRepository.save(cur));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId))
            throw new NotFoundException("Пользователя с id " + userId + " не существует");
        userRepository.deleteById(userId);
    }
}
