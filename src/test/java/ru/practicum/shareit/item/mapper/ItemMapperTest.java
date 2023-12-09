package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemMapperTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final ItemMapper mapper = new ItemMapperImpl();

    @Test
    void mapCreateItemDtoToItem_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapCreateItemDtoToItem(null);
        assertNull(actual);
    }

    @Test
    void mapCreateItemDtoToItem() {
        CreateItemDto expected = CreateItemDto.builder().name("name").description("descr").available(true).build();

        Item actual = mapper.mapCreateItemDtoToItem(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAvailable(), actual.getAvailable());
        assertNull(actual.getId());
    }

    @Test
    void mapUpdateItemDtoToItem_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapUpdateItemDtoToItem(null);
        assertNull(actual);
    }

    @Test
    void mapUpdateItemDtoToItem() {
        UpdateItemDto expected = UpdateItemDto.builder().name("name").description("descr").available(true).build();

        Item actual = mapper.mapUpdateItemDtoToItem(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAvailable(), actual.getAvailable());
        assertNull(actual.getId());
    }

    @Test
    void mapItemToItemDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapItemToItemDto((Item) null);
        assertNull(actual);
    }

    @Test
    void mapItemToItemDto() {
        Item expected = Item.builder().id(1L).name("name").description("descr").available(true).build();

        ItemDto actual = mapper.mapItemToItemDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAvailable(), actual.isAvailable());
        assertEquals(expected.getId(), actual.getId());
    }

    @Test
    void mapItemToItemDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.mapItemToItemDto((List<Item>) null);
        assertNull(actual);
    }

    @Test
    void mapItemToItemDto_List() {
        List<Item> expected = List.of(Item.builder()
                .id(1L)
                .name("name")
                .description("descr")
                .available(true)
                .build());

        var actual = mapper.mapItemToItemDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getAvailable(), actual.get(0).isAvailable());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
    }

    @Test
    void mapItemToItemSimpleDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapItemToItemSimpleDto((Item) null);
        assertNull(actual);
    }

    @Test
    void mapItemToItemSimpleDto() {
        Item expected = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .request(ItemRequest.builder().id(2L).build())
                .build();

        var actual = mapper.mapItemToItemSimpleDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAvailable(), actual.isAvailable());
        assertEquals(expected.getRequest().getId(), actual.getRequestId());

    }

    @Test
    void mapItemToItemSimpleDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.mapItemToItemSimpleDto((List<Item>) null);
        assertNull(actual);
    }

    @Test
    void mapItemToItemSimpleDto_List() {
        List<Item> expected = List.of(Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .request(ItemRequest.builder().id(2L).build())
                .build());

        var actual = mapper.mapItemToItemSimpleDto(expected);

        assertNotNull(actual);
        assertEquals(1, expected.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getName(), actual.get(0).getName());
        assertEquals(expected.get(0).getDescription(), actual.get(0).getDescription());
        assertEquals(expected.get(0).getAvailable(), actual.get(0).isAvailable());
        assertEquals(expected.get(0).getRequest().getId(), actual.get(0).getRequestId());

    }

    @Test
    void mapBookingInfoToDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapBookingInfoToDto(null);
        assertNull(actual);
    }

    @Test
    void mapBookingInfoToDto() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 1, 2, 0, 0, 0);
        Booking expected = Booking.builder()
                .id(1L)
                .status(BookStatus.APPROVED)
                .item(Item.builder().build())
                .booker(User.builder().id(2L).build())
                .start(start)
                .end(end)
                .build();

        var actual = mapper.mapBookingInfoToDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBooker().getId(), actual.getBookerId());
        assertEquals(expected.getStart().format(FORMATTER), actual.getStart());
        assertEquals(expected.getEnd().format(FORMATTER), actual.getEnd());

    }

    @Test
    void mapCommentToCommentDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapCommentToCommentDto(null);
        assertNull(actual);
    }

    @Test
    void mapCommentToCommentDto() {
        LocalDateTime date = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        Comment expected = Comment.builder()
                .id(1L)
                .text("text")
                .author(User.builder().name("name").build())
                .created(date)
                .build();

        var actual = mapper.mapCommentToCommentDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getText(), actual.getText());
        assertEquals(expected.getAuthor().getName(), actual.getAuthorName());
        assertEquals(expected.getCreated().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")), actual.getCreated());
    }

    @Test
    void mapCreateCommentDtoToComment_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapCreateCommentDtoToComment(null);
        assertNull(actual);
    }

    @Test
    void mapCreateCommentDtoToComment() {
        CreateCommentDto expected = CreateCommentDto.builder()
                .text("text")
                .build();

        var actual = mapper.mapCreateCommentDtoToComment(expected);

        assertNotNull(actual);
        assertEquals(expected.getText(), actual.getText());
        assertNull(actual.getId());
        assertNull(actual.getItem());
        assertNull(actual.getAuthor());
        assertNull(actual.getCreated());
    }
}