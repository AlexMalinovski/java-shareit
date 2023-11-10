package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
public class CreateUserDto {

    @NotBlank
    private final String name;

    @NotBlank
    @Email
    private final String email;
}
