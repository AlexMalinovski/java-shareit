package ru.practicum.shareit.library.api.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Email;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class UpdateUserDto {
    private final String name;

    @Email
    private final String email;
}
