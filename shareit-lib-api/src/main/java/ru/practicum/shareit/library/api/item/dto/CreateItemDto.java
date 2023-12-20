package ru.practicum.shareit.library.api.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class CreateItemDto {

    @NotBlank
    private final String name;

    @NotBlank
    private final String description;

    @NotNull
    private final Boolean available;

    private final Long requestId;
}
