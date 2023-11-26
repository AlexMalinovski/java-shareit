package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingInfoDto {
    private final Long id;
    private final Long bookerId;
    private final String start;
    private final String end;
}
