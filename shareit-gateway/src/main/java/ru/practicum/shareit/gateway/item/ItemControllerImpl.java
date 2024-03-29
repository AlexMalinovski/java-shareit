package ru.practicum.shareit.gateway.item;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.item.service.ItemClient;
import ru.practicum.shareit.library.api.item.ItemController;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

@Tag(name = "Вещи", description = "API для работы с вещами")
@RestController
@RequiredArgsConstructor
public class ItemControllerImpl implements ItemController {
    private final ItemClient itemClient;

    @Override
    @Operation(summary = "Добавление новой вещи")
    public ResponseEntity<Object> createItem(long userId, CreateItemDto createItemDto) {
        return itemClient.createItem(userId, createItemDto);
    }

    @Override
    @Operation(summary = "Получение вещи по её id")
    public ResponseEntity<Object> getItemById(long userId, long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @Override
    @Operation(summary = "Редактирование вещи")
    public ResponseEntity<Object> updateItem(long userId, long itemId, UpdateItemDto updateItemDto) {
        return itemClient.updateItem(userId, itemId, updateItemDto);
    }

    @Override
    @Operation(summary = "Просмотр владельцем списка всех его вещей")
    public ResponseEntity<Object> getOwnedItems(long userId, int from, int size) {
        return itemClient.getOwnedItems(userId, from, size);
    }

    @Override
    @Operation(summary = "Поиск вещи арендатором")
    public ResponseEntity<Object> searchItems(long userId, String text, int from, int size) {
        return itemClient.searchItems(userId, text, from, size);
    }

    @Override
    @Operation(summary = "Добавление комментария")
    public ResponseEntity<Object> createComment(long userId, long itemId, CreateCommentDto createCommentDto) {
        return itemClient.createComment(userId, itemId, createCommentDto);
    }
}
