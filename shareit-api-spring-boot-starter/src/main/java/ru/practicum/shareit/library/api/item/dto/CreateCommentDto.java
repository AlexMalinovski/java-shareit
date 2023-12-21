package ru.practicum.shareit.library.api.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
@Jacksonized
public class CreateCommentDto {

    @NotBlank
    private final String text;
}
