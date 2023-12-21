package ru.practicum.shareit.gateway.request.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public interface ItemRequestClient {

    ResponseEntity<Object> createItemRequest(@Valid @Positive long userId, @Valid CreateItemRequestDto dto);

    ResponseEntity<Object> getOwnedItemRequests(@Valid @Positive long ownerId);

    ResponseEntity<Object> getItemRequests(
            @Valid @Positive long userId, @Valid @PositiveOrZero int from, @Valid @Positive int size);

    ResponseEntity<Object> getItemRequestById(@Valid @Positive long userId, @Valid @Positive long requestId);
}
