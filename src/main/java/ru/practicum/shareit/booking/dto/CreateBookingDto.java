package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CreateBookingDto {

    @NotNull
    @Min(1L)
    private final Long itemId;

    @NotEmpty
    private final String start;

    @NotEmpty
    private final String end;
}
