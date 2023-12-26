package ru.practicum.shareit.library.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder(toBuilder = true)
public class CreateUserDto {

    @NotBlank
    @Size(min = 1, max = 255)
    private final String name;

    @NotBlank
    @Email
    @Size(min = 3, max = 512)
    private final String email;
}
