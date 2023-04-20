package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * User model
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private Long id;
    @NotNull
    private String name;
    @Email
    private String email;
}
