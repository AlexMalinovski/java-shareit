package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemSimpleDto;

import java.util.List;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@Builder
public class ItemRequestDto {
    private final Long id;
    private final String description;
    private final String created;
    private final List<ItemSimpleDto> items;
}
