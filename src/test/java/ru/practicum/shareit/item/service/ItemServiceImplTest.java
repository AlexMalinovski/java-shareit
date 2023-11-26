package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemStorage itemStorage;

    @Mock
    private UserStorage userStorage;

    @Mock
    private CommentStorage commentStorage;

    @Mock
    private BookingStorage bookingStorage;

    @InjectMocks
    ItemServiceImpl itemService;

    private Item getValidNewItem() {
        return Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private Item getValidItem() {
        return Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(getValidUser())
                .build();
    }

    private User getValidUser() {
        return User.builder()
                .id(1L)
                .name("name")
                .email("name@e.mail")
                .build();
    }

    @Test
    void setOwnerAndCreateItem_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.setOwnerAndCreateItem(getValidNewItem(), 1L));

        verify(userStorage).findById(1L);
    }

    @Test
    void setOwnerAndCreateItem_ifUserFound_thenCreate() {
        User expectedUser = getValidUser();
        Item expectedItem = getValidItem()
                .toBuilder()
                .owner(expectedUser)
                .build();
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemStorage.save(any())).thenReturn(expectedItem);

        var actual = itemService.setOwnerAndCreateItem(getValidItem(), 1L);

        verify(userStorage).findById(1L);
        verify(itemStorage).save(expectedItem);
        assertEquals(expectedItem, actual);
    }

    @Test
    void getItemById_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));

        verify(userStorage).existsById(1L);
    }

    @Test
    void getItemById_ifItemNotFound_thenReturnEmpty() {
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.empty());

        var actual = itemService.getItemById(2L, 1L);

        verify(userStorage).existsById(1L);
        verify(itemStorage).findById(2L);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void getItemById_thenReturnFound() {
        var expected = getValidItem();
        List<Comment> comments = new ArrayList<>();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(expected));
        when(commentStorage.findAllByItem_IdOrderByCreatedAsc(anyLong())).thenReturn(comments);
        when(bookingStorage.findTopOrderByTime(any(BooleanExpression.class), any())).thenReturn(Optional.empty());


        var actual = itemService.getItemById(1L, 1L);

        expected = expected.toBuilder().comments(comments).build();
        verify(userStorage).existsById(1L);
        verify(itemStorage).findById(1L);
        verify(commentStorage).findAllByItem_IdOrderByCreatedAsc(expected.getId());
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void checkOwnerAndUpdateItem_ifItemNotFound_thenThrowNotFoundException() {
        Item expectedItem = getValidItem();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.checkOwnerAndUpdateItem(expectedItem,1L));

        verify(itemStorage).findById(expectedItem.getId());
    }

    @Test
    void checkOwnerAndUpdateItem_ifUserNotOwner_thenThrowNotFoundException() {
        Item expectedItem = getValidItem()
                .toBuilder()
                .owner(getValidUser())
                .build();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(expectedItem));

        assertThrows(NotFoundException.class, () -> itemService.checkOwnerAndUpdateItem(expectedItem,1000L));

        verify(itemStorage).findById(expectedItem.getId());
    }

    @Test
    void checkOwnerAndUpdateItem_updateItem() {
        Item oldItem = getValidItem()
                .toBuilder()
                .owner(getValidUser())
                .build();
        Item expectedItem = getValidItem()
                .toBuilder()
                .owner(getValidUser())
                .name("newname").build();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(oldItem));
        when(itemStorage.save(any())).thenReturn(expectedItem);

        var actual = itemService.checkOwnerAndUpdateItem(
                Item.builder().id(1L).name("newname").build(), oldItem.getOwner().getId());

        verify(itemStorage).findById(oldItem.getId());
        verify(itemStorage).save(expectedItem);
        assertEquals(expectedItem, actual);
    }

    @Test
    void getOwnedItems_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.getOwnedItems(1L));

        verify(userStorage).existsById(1L);
    }

    @Test
    void getOwnedItems_ifUserFound_thenReturnList() {
        User user = getValidUser();
        Item expectedItem = getValidItem()
                .toBuilder()
                .owner(user)
                .build();
        List<Comment> comments = new ArrayList<>();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findByOwner_IdOrderById(anyLong())).thenReturn(List.of(expectedItem));
        when(commentStorage.findAllByItem_IdOrderByCreatedAsc(anyLong())).thenReturn(comments);
        when(bookingStorage.findTopOrderByTime(any(BooleanExpression.class), any())).thenReturn(Optional.empty());

        var actual = itemService.getOwnedItems(user.getId());

        expectedItem = expectedItem.toBuilder().comments(comments).build();
        verify(userStorage).existsById(user.getId());
        verify(itemStorage).findByOwner_IdOrderById(user.getId());
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expectedItem, actual.get(0));
    }

    @Test
    void getAvailableItemsBySubString_ifUserFound_thenReturnList() {
        User user = getValidUser();
        Item expectedItem = getValidItem()
                .toBuilder()
                .owner(user)
                .build();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemStorage.findAllByExpression(any(BooleanExpression.class))).thenReturn(List.of(expectedItem));
        BooleanExpression byTextInNameOrDescription = QItem.item.name.containsIgnoreCase("abc")
                .or(QItem.item.description.containsIgnoreCase("abc"));
        BooleanExpression byAvailableTrue = QItem.item.available.isTrue();

        var actual = itemService.getAvailableItemsBySubString("abc", user.getId());

        verify(userStorage).existsById(user.getId());
        verify(itemStorage).findAllByExpression(byAvailableTrue.and(byTextInNameOrDescription));
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expectedItem, actual.get(0));
    }

    @Test
    void getAvailableItemsBySubString_ifUserNotFound_thenThrowNotFoundException() {
        User user = getValidUser();
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemService.getAvailableItemsBySubString("abc", user.getId()));

        verify(userStorage).existsById(user.getId());
    }

    @Test
    void getAvailableItemsBySubString_ifUserFoundAndEmptyText_thenReturnEmptyList() {
        User user = getValidUser();
        when(userStorage.existsById(anyLong())).thenReturn(true);

        var actual = itemService.getAvailableItemsBySubString("", user.getId());

        verify(userStorage).existsById(user.getId());
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }

    @Test
    void checkAuthorItemAndCreateComment_ifNotFoundItemBooking_thenThrowBadRequestException() {
        when(bookingStorage.findAny(any(BooleanExpression.class))).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class,
                () -> itemService.checkAuthorItemAndCreateComment(1L, 1L, Comment.builder().build()));

        verify(bookingStorage).findAny(any());
    }

    @Test
    void checkAuthorItemAndCreateComment_saveComment() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(BookStatus.APPROVED)
                .item(getValidItem())
                .booker(getValidUser())
                .build();
        Comment expected = Comment.builder()
                .author(booking.getBooker())
                .item(booking.getItem())
                .text("abc")
                .build();
        when(bookingStorage.findAny(any(BooleanExpression.class))).thenReturn(Optional.of(booking));
        when(commentStorage.save(any())).thenReturn(expected.toBuilder().id(1L).build());

        itemService.checkAuthorItemAndCreateComment(
                booking.getBooker().getId(), booking.getItem().getId(), Comment.builder().text(expected.getText()).build());

        verify(bookingStorage).findAny(any());
        verify(commentStorage).save(expected);
    }
}