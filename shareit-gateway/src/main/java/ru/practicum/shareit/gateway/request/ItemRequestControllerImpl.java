package ru.practicum.shareit.gateway.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.request.service.ItemRequestClient;
import ru.practicum.shareit.library.api.request.ItemRequestController;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;

@Tag(name = "Запросы вещей", description = "API для работы с запросами на добавление вещей")
@RestController
@RequiredArgsConstructor
public class ItemRequestControllerImpl implements ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @Override
    @Operation(summary = "Добавить новый запрос")
    public ResponseEntity<Object> createItemRequest(long userId, CreateItemRequestDto dto) {
        return itemRequestClient.createItemRequest(userId, dto);
    }

    @Override
    @Operation(summary = "Получить список своих запросов")
    public ResponseEntity<Object> getOwnedItemRequests(long ownerId) {
        return itemRequestClient.getOwnedItemRequests(ownerId);
    }

    @Override
    @Operation(summary = "Получить список запросов, созданных другими пользователями")
    public ResponseEntity<Object> getItemRequests(long userId, int from, int size) {
        return itemRequestClient.getItemRequests(userId, from, size);
    }

    @Override
    @Operation(summary = "Получить данные о запросе по id")
    public ResponseEntity<Object> getItemRequestById(long userId, long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
