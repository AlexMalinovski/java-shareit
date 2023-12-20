package ru.practicum.shareit.request.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.library.api.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ItemMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface ItemRequestMapper {
    ItemRequest mapCreateItemRequestDtoToItemRequest(CreateItemRequestDto dto);

    @Mapping(target = "created", source = "itemRequest.created", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    ItemRequestDto mapItemRequestToItemRequestDto(ItemRequest itemRequest);

    List<ItemRequestDto> mapItemRequestToItemRequestDto(List<ItemRequest> itemRequests);
}
