package ru.practicum.shareit.library.api.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.library.api.item.dto.ItemSimpleDto;
import ru.practicum.shareit.library.api.user.dto.UserDto;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class BookingDto {
    private final Long id;
    private final String start;
    private final String end;
    private final String status;
    private final UserDto booker;
    private final ItemSimpleDto item;
}
