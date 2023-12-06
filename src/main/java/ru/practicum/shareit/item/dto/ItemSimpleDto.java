package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemSimpleDto {
    private final long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Long requestId;
}