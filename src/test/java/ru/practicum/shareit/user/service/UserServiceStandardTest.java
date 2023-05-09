package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.AlreadyExistsException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class UserServiceStandardTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceStandard userServiceStandard;

    private static final User myUser = User.builder()
            .id(1L)
            .email("user@gmail.com")
            .name("John")
            .build();

    @Test
    void list() {
        when(userRepository.findAll()).thenReturn(List.of(myUser));
        List<UserDto> expectedList = Stream.of(myUser).map(UserMapper::toDto).collect(Collectors.toList());

        assertEquals(expectedList, userServiceStandard.list());
    }

    @Test
    void getUserById_whenUserIsExists_thenReturnUserDto() {
        when(userRepository.findById(myUser.getId())).thenReturn(Optional.of(myUser));

        assertEquals(UserMapper.toDto(myUser), userServiceStandard.getUserById(myUser.getId()));
    }

    @Test
    void getUserById_whenUserIsNotExists_thenThrowNotFoundException() {
        when(userRepository.findById(myUser.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userServiceStandard.getUserById(myUser.getId()));
    }

    @Test
    void addUser_whenUserIsValid_thenReturnUserDto() {
        when(userRepository.save(myUser)).thenReturn(myUser);
        UserDto expected = UserMapper.toDto(myUser);

        UserDto result = userServiceStandard.addUser(myUser);

        assertEquals(expected, result);
    }

    @Test
    void addUser_whenUserEmailIsNull_thenThrowValidationException() {
        User current = User.builder()
                .id(myUser.getId())
                .email(null)
                .name(myUser.getName())
                .build();

        assertThrows(ValidationException.class, () -> userServiceStandard.addUser(current));
    }

    @Test
    void addUser_whenUserNameIsNull_thenThrowValidationException() {
        User current = User.builder()
                .id(myUser.getId())
                .email(myUser.getEmail())
                .name(null)
                .build();

        assertThrows(ValidationException.class, () -> userServiceStandard.addUser(current));
    }

    @Test
    void updateUser_whenUserExistsAndEmailValid_thenReturnUserDto() {
        when(userRepository.findById(myUser.getId())).thenReturn(Optional.of(myUser));
        when(userRepository.save(myUser)).thenReturn(myUser);
        when(userRepository.existsByEmail(myUser.getEmail())).thenReturn(true);
        UserDto expected = UserMapper.toDto(myUser);

        assertEquals(expected, userServiceStandard.updateUser(myUser.getId(), myUser));
    }


    @Test
    void updateUser_whenEmailNotValid_thenThrowAlreadyExistsException() {
        when(userRepository.findById(myUser.getId())).thenReturn(Optional.of(myUser));
        String newEmailWitchAlreadyExists = "alreadyUsed@gmail.com";
        User currentUser = User.builder()
                .id(myUser.getId())
                .email(newEmailWitchAlreadyExists)
                .name(myUser.getName())
                .build();
        when(userRepository.existsByEmail(currentUser.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> userServiceStandard.updateUser(myUser.getId(), currentUser));
    }

    @Test
    void updateUser_whenUserNotExists_thenThrowNotFoundException() {
        when(userRepository.findById(myUser.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userServiceStandard.updateUser(myUser.getId(), myUser));
    }

    @Test
    void deleteUser_whenUserExists_thenDoNotThrow() {
        when(userRepository.existsById(myUser.getId())).thenReturn(true);

        assertDoesNotThrow(() -> userServiceStandard.deleteUser(myUser.getId()));
    }

    @Test
    void deleteUser_whenUserNotExists_thenThrowNotFoundException() {
        when(userRepository.existsById(myUser.getId())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userServiceStandard.deleteUser(myUser.getId()));
    }

}