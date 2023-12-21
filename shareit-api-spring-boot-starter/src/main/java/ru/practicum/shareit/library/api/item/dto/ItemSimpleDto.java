package ru.practicum.shareit.library.api.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class ItemSimpleDto {
    private final long id;
    private final String name;
    private final String description;
    private final boolean available;
    private final Long requestId;
}
