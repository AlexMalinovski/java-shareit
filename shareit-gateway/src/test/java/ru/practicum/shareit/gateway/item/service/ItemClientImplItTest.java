package ru.practicum.shareit.gateway.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ItemClientImplItTest {

    @Autowired
    private ItemClient itemClient;

    @Test
    void createItem_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                -1L, CreateItemDto.builder().name("name").description("descr").available(true).build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                0L, CreateItemDto.builder().name("name").description("descr").available(true).build()));
    }

    @Test
    void createItem_ifInvalidDto_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                1L, CreateItemDto.builder().description("descr").available(true).build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                1L, CreateItemDto.builder().name("").description("descr").available(true).build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                1L, CreateItemDto.builder().name(" ").description("descr").available(true).build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                1L, CreateItemDto.builder().name("a".repeat(256)).description("descr").available(true).build()));

        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                0L, CreateItemDto.builder().name("name").available(true).build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                0L, CreateItemDto.builder().name("name").description("").available(true).build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                0L, CreateItemDto.builder().name("name").description(" ").available(true).build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                0L, CreateItemDto.builder().name("name").description("a".repeat(513)).available(true).build()));

        assertThrows(ConstraintViolationException.class, () -> itemClient.createItem(
                0L, CreateItemDto.builder().name("name").description("descr").build()));
    }

    @Test
    void getItemById_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.getItemById(-1L,1L));
        assertThrows(ConstraintViolationException.class, () -> itemClient.getItemById(0L,1L));
    }

    @Test
    void getItemById_ifInvalidItemId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.getItemById(1L,-1L));
        assertThrows(ConstraintViolationException.class, () -> itemClient.getItemById(1L,0L));
    }

    @Test
    void updateItem_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                -1L,1L, UpdateItemDto.builder().name("new").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                0L,1L, UpdateItemDto.builder().name("new").build()));
    }

    @Test
    void updateItem_ifInvalidItemId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,-1L, UpdateItemDto.builder().name("new").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,0L, UpdateItemDto.builder().name("new").build()));
    }

    @Test
    void updateItem_ifInvalidDto_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,1L, UpdateItemDto.builder().name("").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,1L, UpdateItemDto.builder().name(" ").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,1L, UpdateItemDto.builder().name("a".repeat(256)).build()));

        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,1L, UpdateItemDto.builder().description("").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,1L, UpdateItemDto.builder().description(" ").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.updateItem(
                1L,1L, UpdateItemDto.builder().description("a".repeat(513)).build()));
    }

    @Test
    void getOwnedItems_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.getOwnedItems(-1L,0, 1));
        assertThrows(ConstraintViolationException.class, () -> itemClient.getOwnedItems(0L,0, 1));
    }

    @Test
    void getOwnedItems_ifInvalidFrom_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.getOwnedItems(1L,-1, 1));
    }

    @Test
    void getOwnedItems_ifInvalidSize_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.getOwnedItems(1L,0, -1));
        assertThrows(ConstraintViolationException.class, () -> itemClient.getOwnedItems(1L,0, 0));
    }

    @Test
    void searchItems_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.searchItems(-1L, "a",0, 1));
        assertThrows(ConstraintViolationException.class, () -> itemClient.searchItems(0L, "a", 0, 1));
    }

    @Test
    void searchItems_ifInvalidFrom_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.searchItems(1L, "a",-1, 1));
    }

    @Test
    void searchItems_ifInvalidSize_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.searchItems(1L, "a",0, -1));
        assertThrows(ConstraintViolationException.class, () -> itemClient.searchItems(1L, "a",0, 0));
    }


    @Test
    void createComment_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.createComment(
                -1L, 1L, CreateCommentDto.builder().text("text").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createComment(
                0L, 1L, CreateCommentDto.builder().text("text").build()));
    }

    @Test
    void createComment_ifInvalidItemId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> itemClient.createComment(
                1L, -1L, CreateCommentDto.builder().text("text").build()));
        assertThrows(ConstraintViolationException.class, () -> itemClient.createComment(
                1L, 0L, CreateCommentDto.builder().text("text").build()));
    }
}