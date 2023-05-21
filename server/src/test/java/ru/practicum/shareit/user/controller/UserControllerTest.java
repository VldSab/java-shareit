package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    private static final User myUser = User.builder()
            .id(1L)
            .email("user@gmail.com")
            .name("John")
            .build();

    private static final UserDto myUserDto = UserMapper.toDto(myUser);

    @Test
    void listUsers_whenInvoked_thenReturnListOfUserDto() {
        List<UserDto> userDtoList = List.of(myUserDto);
        when(userService.list()).thenReturn(userDtoList);

        assertEquals(userDtoList, userController.listUsers());
    }

    @Test
    void getUser_whenInvoked_thenReturnUserDto() {
        when(userService.getUserById(myUser.getId())).thenReturn(myUserDto);

        assertEquals(myUserDto, userController.getUser(myUser.getId()));
    }

    @Test
    void addUser_whenInvoked_thenReturnUserDto() {
        when(userService.addUser(myUser)).thenReturn(myUserDto);

        assertEquals(myUserDto, userController.addUser(myUser));
    }

    @Test
    void updateUser_whenInvoked_thenReturnUserDto() {
        when(userService.updateUser(myUser.getId(), myUser)).thenReturn(myUserDto);

        assertEquals(myUserDto, userController.updateUser(myUser.getId(), myUser));
    }

    @Test
    void deleteUser_whenInvoked_thenReturnTrue() {
        assertTrue(userController.deleteUser(myUser.getId()));
    }
}