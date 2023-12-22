package ru.practicum.shareit.library.api.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
@Jacksonized
public class CreateItemDto {

    @NotBlank
    @Size(max = 255)
    private final String name;

    @NotBlank
    @Size(max = 512)
    private final String description;

    @NotNull
    private final Boolean available;

    private final Long requestId;
}
