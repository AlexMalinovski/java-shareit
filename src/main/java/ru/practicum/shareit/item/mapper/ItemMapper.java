package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingInfoDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "request",
            expression = "java(createItemDto.getRequestId() == null ? null : ru.practicum.shareit.request.model.ItemRequest.builder().id(createItemDto.getRequestId()).build())")
    Item mapCreateItemDtoToItem(CreateItemDto createItemDto);

    Item mapUpdateItemDtoToItem(UpdateItemDto updateItemDto);

    @Mapping(target = "requestId", expression = "java(item.getRequest() == null ? null : item.getRequest().getId())")
    ItemSimpleDto mapItemToItemSimpleDto(Item item);

    List<ItemSimpleDto> mapItemToItemSimpleDto(List<Item> item);

    ItemDto mapItemToItemDto(Item item);

    List<ItemDto> mapItemToItemDto(List<Item> items);

    @Mapping(target = "bookerId", expression = "java(booking.getBooker().getId())")
    @Mapping(target = "start", source = "start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    BookingInfoDto mapBookingInfoToDto(Booking booking);

    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    @Mapping(target = "created", source = "created", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    CommentDto mapCommentToCommentDto(Comment comment);

    Comment mapCreateCommentDtoToComment(CreateCommentDto dto);
}
