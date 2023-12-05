package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Data
@Builder
public class ItemRequestDto {
    private final Long id;
    private final String description;
    private final String created;
    private final List<ItemDto> items;
}
