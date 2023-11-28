package ru.practicum.shareit.item.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.BookingInfoDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    Item mapCreateItemDtoToItem(CreateItemDto createItemDto);

    Item mapUpdateItemDtoToItem(UpdateItemDto updateItemDto);

    ItemDto mapItemToItemDto(Item item);

    @Mapping(target = "bookerId", expression = "java(booking.getBooker().getId())")
    @Mapping(target = "start", source = "start", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    @Mapping(target = "end", source = "end", dateFormat = "yyyy-MM-dd'T'HH:mm:ss")
    BookingInfoDto mapBookingInfoToDto(Booking booking);

    @Mapping(target = "authorName", expression = "java(comment.getAuthor().getName())")
    @Mapping(target = "created", source = "created", dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    CommentDto mapCommentToCommentDto(Comment comment);

    Comment mapCreateCommentDtoToComment(CreateCommentDto dto);
}
