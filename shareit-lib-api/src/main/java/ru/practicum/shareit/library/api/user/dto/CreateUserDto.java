package ru.practicum.shareit.library.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class CreateUserDto {

    @NotBlank
    private final String name;

    @NotBlank
    @Email
    private final String email;
}
