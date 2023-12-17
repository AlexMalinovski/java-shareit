package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class BookingInfoDto {
    private final Long id;
    private final Long bookerId;
    private final String start;
    private final String end;
}
