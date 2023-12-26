package ru.practicum.shareit.gateway.item.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public interface ItemClient {
    ResponseEntity<Object> createItem(@Valid @Positive long userId, @Valid CreateItemDto createItemDto);

    ResponseEntity<Object> getItemById(@Valid @Positive long userId, @Valid @Positive long itemId);

    ResponseEntity<Object> updateItem(
            @Valid @Positive long userId, @Valid @Positive long itemId, @Valid UpdateItemDto updateItemDto);

    ResponseEntity<Object> getOwnedItems(
            @Valid @Positive long userId, @Valid @PositiveOrZero int from, @Valid @Positive int size);

    ResponseEntity<Object> searchItems(
            @Valid @Positive long userId, String text, @Valid @PositiveOrZero int from, @Valid @Positive int size);

    ResponseEntity<Object> createComment(
            @Valid @Positive long userId, @Valid @Positive long itemId, @Valid CreateCommentDto createCommentDto);
}
