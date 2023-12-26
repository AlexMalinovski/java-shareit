package ru.practicum.shareit.gateway.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ItemRequestClientImplItTest {

    @Autowired
    private ItemRequestClient itemRequestClient;

    @Test
    void createItemRequest_ifInvalidId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.createItemRequest(
                -1L, CreateItemRequestDto.builder().description("text").build()));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.createItemRequest(
                0L, CreateItemRequestDto.builder().description("text").build()));
    }

    @Test
    void createItemRequest_ifInvalidDto_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.createItemRequest(
                1L, CreateItemRequestDto.builder().description("").build()));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.createItemRequest(
                1L, CreateItemRequestDto.builder().description(" ").build()));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.createItemRequest(
                1L, CreateItemRequestDto.builder().description("a".repeat(513)).build()));
    }

    @Test
    void getOwnedItemRequests_ifInvalidOwnerId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getOwnedItemRequests(-1L));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getOwnedItemRequests(0L));
    }

    @Test
    void getItemRequests_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequests(-1L, 0, 1));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequests(0L, 0, 1));
    }

    @Test
    void getItemRequests_ifInvalidFrom_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequests(1L, -1, 1));
    }

    @Test
    void getItemRequests_ifInvalidSize_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequests(1L, 0, -1));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequests(1L, 0, 0));
    }

    @Test
    void getItemRequestById_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequestById(-1L, 1L));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequestById(0L, 1L));
    }

    @Test
    void getItemRequestById_ifInvalidRequestId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequestById(1L, -1L));
        assertThrows(ConstraintViolationException.class, () -> itemRequestClient.getItemRequestById(1L, 0L));
    }
}