package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * User model
 */
@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
    private Long id;
    @NotBlank
    private String name;
    @Email
    private String email;
}
