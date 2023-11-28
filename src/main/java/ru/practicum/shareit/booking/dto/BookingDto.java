package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

@Data
@Builder
public class BookingDto {
    private final Long id;
    private final String start;
    private final String end;
    private final String status;
    private final UserDto booker;
    private final ItemDto item;
}
