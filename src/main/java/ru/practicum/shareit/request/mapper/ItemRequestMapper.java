package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class})
public interface ItemRequestMapper {
    ItemRequest mapCreateItemRequestDtoToItemRequest(CreateItemRequestDto dto);
    ItemRequestDto mapItemRequestToItemRequestDto(ItemRequest itemRequest);
    List<ItemRequestDto> mapItemRequestToItemRequestDto(List<ItemRequest> itemRequests);
}
