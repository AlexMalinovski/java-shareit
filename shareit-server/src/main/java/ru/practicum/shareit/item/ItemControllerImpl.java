package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.library.api.exception.NotFoundException;
import ru.practicum.shareit.library.api.item.ItemController;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.ItemDto;
import ru.practicum.shareit.library.api.item.dto.ItemSimpleDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemControllerImpl implements ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Override
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestBody CreateItemDto createItemDto) {
        final Item item = itemMapper.mapCreateItemDtoToItem(createItemDto);
        Item createdItem = itemService.setOwnerAndCreateItem(item, userId);
        return ResponseEntity.ok(itemMapper.mapItemToItemSimpleDto(createdItem));
    }

    @Override
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @PathVariable long itemId) {
        Item item = itemService.getItemById(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));
        return ResponseEntity.ok(itemMapper.mapItemToItemDto(item));
    }

    @Override
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @RequestBody UpdateItemDto updateItemDto) {
        final Item item = itemMapper.mapUpdateItemDtoToItem(updateItemDto)
                .toBuilder()
                .id(itemId)
                .build();
        Item updatedItem = itemService.checkOwnerAndUpdateItem(item, userId);
        return ResponseEntity.ok(itemMapper.mapItemToItemDto(updatedItem));
    }

    @Override
    public ResponseEntity<Object> getOwnedItems(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size) {

        List<ItemDto> items = itemMapper.mapItemToItemDto(
                itemService.getOwnedItems(userId, from, size));
        return ResponseEntity.ok(items);
    }

    @Override
    public ResponseEntity<Object> searchItems(
            @RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size) {

        List<ItemSimpleDto> items = itemMapper.mapItemToItemSimpleDto(
                itemService.getAvailableItemsBySubString(text, userId, from, size));
        return ResponseEntity.ok(items);
    }

    @Override
    public ResponseEntity<Object> createComment(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @PathVariable long itemId,
            @RequestBody CreateCommentDto createCommentDto) {

        Comment comment = itemMapper.mapCreateCommentDtoToComment(createCommentDto);
        Comment created = itemService.checkAuthorItemAndCreateComment(userId, itemId, comment);
        return ResponseEntity.ok(itemMapper.mapCommentToCommentDto(created));
    }
}
