package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;

/**
 * User model
 */
@Data
@SuperBuilder
public class User {
    private Long id;
    private String name;
    @Email
    private String email;
}
