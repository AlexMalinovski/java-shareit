package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemStorage itemStorage;

    @Mock
    private UserStorage userStorage;

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
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.setOwnerAndCreateItem(getValidNewItem(), 1L));

        verify(userStorage).getUserById(1L);
    }

    @Test
    void setOwnerAndCreateItem_ifUserFound_thenCreate() {
        User expectedUser = getValidUser();
        Item expectedItem = getValidItem();
        expectedItem.setOwner(expectedUser);
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.of(expectedUser));
        when(itemStorage.createItem(any())).thenReturn(expectedItem);

        var actual = itemService.setOwnerAndCreateItem(getValidItem(), 1L);

        verify(userStorage).getUserById(1L);
        verify(itemStorage).createItem(expectedItem);
        assertEquals(expectedItem, actual);
    }

    @Test
    void getItemById_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));

        verify(userStorage).getUserById(1L);
    }

    @Test
    void getItemById_ifItemNotFound_thenReturnEmpty() {
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.of(getValidUser()));
        when(itemStorage.getItemById(anyLong())).thenReturn(Optional.empty());

        var actual = itemService.getItemById(2L, 1L);

        verify(userStorage).getUserById(1L);
        verify(itemStorage).getItemById(2L);
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    void getItemById_thenReturnFound() {
        var expected = getValidItem();
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.of(getValidUser()));
        when(itemStorage.getItemById(anyLong())).thenReturn(Optional.of(expected));

        var actual = itemService.getItemById(1L, 1L);

        verify(userStorage).getUserById(1L);
        verify(itemStorage).getItemById(1L);
        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    void checkOwnerAndUpdateItem_ifItemNotFound_thenThrowNotFoundException() {
        Item expectedItem = getValidItem();
        when(itemStorage.getItemById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.checkOwnerAndUpdateItem(expectedItem,1L));

        verify(itemStorage).getItemById(expectedItem.getId());
    }

    @Test
    void checkOwnerAndUpdateItem_ifUserNotOwner_thenThrowNotFoundException() {
        Item expectedItem = getValidItem();
        expectedItem.setOwner(getValidUser());
        when(itemStorage.getItemById(anyLong())).thenReturn(Optional.of(expectedItem));

        assertThrows(NotFoundException.class, () -> itemService.checkOwnerAndUpdateItem(expectedItem,1000L));

        verify(itemStorage).getItemById(expectedItem.getId());
    }

    @Test
    void checkOwnerAndUpdateItem_updateItem() {
        Item oldItem = getValidItem();
        oldItem.setOwner(getValidUser());
        Item expectedItem = oldItem.copyOf();
        expectedItem.setName("newname");
        when(itemStorage.getItemById(anyLong())).thenReturn(Optional.of(oldItem));
        when(itemStorage.updateItem(any())).thenReturn(expectedItem);

        var actual = itemService.checkOwnerAndUpdateItem(
                Item.builder().id(1L).name("newname").build(), oldItem.getOwner().getId());

        verify(itemStorage).getItemById(oldItem.getId());
        verify(itemStorage).updateItem(expectedItem);
        assertEquals(expectedItem, actual);
    }

    @Test
    void getOwnedItems_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> itemService.getOwnedItems(1L));

        verify(userStorage).getUserById(1L);
    }

    @Test
    void getOwnedItems_ifUserFound_thenReturnList() {
        User user = getValidUser();
        Item expectedItem = getValidItem();
        expectedItem.setOwner(user);
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.getItemsByOwnerId(anyLong())).thenReturn(List.of(expectedItem));

        var actual = itemService.getOwnedItems(user.getId());

        verify(userStorage).getUserById(user.getId());
        verify(itemStorage).getItemsByOwnerId(user.getId());
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expectedItem, actual.get(0));
    }

    @Test
    void getAvailableItemsBySubString_ifUserFound_thenReturnList() {
        User user = getValidUser();
        Item expectedItem = getValidItem();
        expectedItem.setOwner(user);
        when(userStorage.getUserById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.getAvailableItemsBySubString(anyString())).thenReturn(List.of(expectedItem));

        var actual = itemService.getAvailableItemsBySubString("abc", user.getId());

        verify(userStorage).getUserById(user.getId());
        verify(itemStorage).getAvailableItemsBySubString("abc");
        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expectedItem, actual.get(0));
    }
}