package ru.practicum.shareit.library.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class UpdateUserDto {

    @Size(min = 1, max = 255)
    @Pattern(regexp = "^(.*)\\S+(.*)$")
    private final String name;

    @Email
    @Size(min = 3, max = 512)
    private final String email;
}
