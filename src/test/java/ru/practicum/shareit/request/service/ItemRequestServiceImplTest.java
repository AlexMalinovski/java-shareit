package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.QItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    private ItemRequestStorage itemRequestStorage;

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private final ArgumentCaptor<ItemRequest> requestCaptor = ArgumentCaptor.forClass(ItemRequest.class);

    @Test
    void createItemRequest_ifUserNotFound_thenThrowNotFoundException() {
        ItemRequest itemRequest = ItemRequest.builder().build();
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.createItemRequest(itemRequest, 1L));

        verify(userStorage, never()).save(any());
    }

    @Test
    void createItemRequest() {
        long userId = 1L;
        ItemRequest expected = ItemRequest.builder().description("description").build();
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemRequestStorage.save(any())).thenReturn(expected);

        var actual = itemRequestService.createItemRequest(expected, userId);

        verify(itemRequestStorage).save(requestCaptor.capture());
        assertNotNull(requestCaptor.getValue());
        assertEquals(expected.getDescription(), requestCaptor.getValue().getDescription());
        assertEquals(userId, requestCaptor.getValue().getUser().getId());
        assertEquals(expected, actual);
    }

    @Test
    void getOwnedItemRequests_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.getOwnedItemRequests(1L));
    }


    @Test
    void getOwnedItemRequests() {
        List<ItemRequest> expected = List.of(ItemRequest.builder().build());
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemRequestStorage.findAllFetchItems(any(), any())).thenReturn(expected);

        var actual = itemRequestService.getOwnedItemRequests(1L);

        assertEquals(expected, actual);
        verify(itemRequestStorage).findAllFetchItems(
                QItemRequest.itemRequest.user.id.eq(1L),
                QItemRequest.itemRequest.created.desc());

    }

    @Test
    void getItemRequests_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequests(1L, 0, 10));
    }

    @Test
    void getItemRequests() {
        List<ItemRequest> expected = List.of(ItemRequest.builder().build());
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemRequestStorage.findAllFetchItemsPagination(any(), any(), anyInt(), anyInt())).thenReturn(expected);

        var actual = itemRequestService.getItemRequests(1L, 0, 10);

        assertEquals(expected, actual);
        verify(itemRequestStorage).findAllFetchItemsPagination(
                QItemRequest.itemRequest.user.id.eq(1L).not(),
                QItemRequest.itemRequest.created.desc(),
                0, 10);

    }

    @Test
    void getItemRequestById_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> itemRequestService.getItemRequestById(1L, 2L));
    }

    @Test
    void getItemRequestById() {
        Optional<ItemRequest> expected = Optional.of(ItemRequest.builder().build());
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(itemRequestStorage.findAnyFetch(any())).thenReturn(expected);

        var actual = itemRequestService.getItemRequestById(1L, 2L);

        assertEquals(expected, actual);
        verify(itemRequestStorage).findAnyFetch(
                QItemRequest.itemRequest.id.eq(1L));
    }
}