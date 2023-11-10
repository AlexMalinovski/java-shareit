package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    Item mapCreateItemDtoToItem(CreateItemDto createItemDto);
    Item mapUpdateItemDtoToItem(UpdateItemDto updateItemDto);
    ItemDto mapItemToItemDto(Item item);
}
