package ru.practicum.shareit.gateway.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.item.service.ItemClient;
import ru.practicum.shareit.library.api.item.ItemController;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

@RestController
@RequiredArgsConstructor
public class ItemControllerImpl implements ItemController {
    private final ItemClient itemClient;

    @Override
    public ResponseEntity<Object> createItem(long userId, CreateItemDto createItemDto) {
        return itemClient.createItem(userId, createItemDto);
    }

    @Override
    public ResponseEntity<Object> getItemById(long userId, long itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @Override
    public ResponseEntity<Object> updateItem(long userId, long itemId, UpdateItemDto updateItemDto) {
        return itemClient.updateItem(userId, itemId, updateItemDto);
    }

    @Override
    public ResponseEntity<Object> getOwnedItems(long userId, int from, int size) {
        return itemClient.getOwnedItems(userId, from, size);
    }

    @Override
    public ResponseEntity<Object> searchItems(long userId, String text, int from, int size) {
        return itemClient.searchItems(userId, text, from, size);
    }

    @Override
    public ResponseEntity<Object> createComment(long userId, long itemId, CreateCommentDto createCommentDto) {
        return itemClient.createComment(userId, itemId, createCommentDto);
    }
}
