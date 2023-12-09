package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class ItemDto {
    private final long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final BookingInfoDto lastBooking;
    private final BookingInfoDto nextBooking;
    private final List<CommentDto> comments;
//    private final Long requestId;
}
