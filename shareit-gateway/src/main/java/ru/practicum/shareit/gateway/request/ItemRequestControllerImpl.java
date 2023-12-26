package ru.practicum.shareit.gateway.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.request.service.ItemRequestClient;
import ru.practicum.shareit.library.api.request.ItemRequestController;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;

@RestController
@RequiredArgsConstructor
public class ItemRequestControllerImpl implements ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @Override
    public ResponseEntity<Object> createItemRequest(long userId, CreateItemRequestDto dto) {
        return itemRequestClient.createItemRequest(userId, dto);
    }

    @Override
    public ResponseEntity<Object> getOwnedItemRequests(long ownerId) {
        return itemRequestClient.getOwnedItemRequests(ownerId);
    }

    @Override
    public ResponseEntity<Object> getItemRequests(long userId, int from, int size) {
        return itemRequestClient.getItemRequests(userId, from, size);
    }

    @Override
    public ResponseEntity<Object> getItemRequestById(long userId, long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
