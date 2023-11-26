package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemDto {
    private final long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final BookingInfoDto lastBooking;
    private final BookingInfoDto nextBooking;
    private final List<CommentDto> comments;
}
