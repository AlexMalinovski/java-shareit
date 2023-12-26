package ru.practicum.shareit.library.api.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
@Jacksonized
public class CreateBookingDto {

    @NotNull
    @Min(1L)
    private final Long itemId;

    @NotEmpty
    private final String start;

    @NotEmpty
    private final String end;
}
