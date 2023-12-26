package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.library.api.exception.NotFoundException;
import ru.practicum.shareit.library.api.request.ItemRequestController;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Validated
public class ItemRequestControllerImpl implements ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;


    @Override
    public ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody CreateItemRequestDto dto) {

        ItemRequest itemRequest = itemRequestMapper.mapCreateItemRequestDtoToItemRequest(dto);
        ItemRequest created = itemRequestService.createItemRequest(itemRequest, userId);
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(created));
    }

    @Override
    public ResponseEntity<Object> getOwnedItemRequests(@RequestHeader("X-Sharer-User-Id") long ownerId) {

        List<ItemRequest> requests = itemRequestService.getOwnedItemRequests(ownerId);
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(requests));
    }

    @Override
    public ResponseEntity<Object> getItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size) {

        List<ItemRequest> requests =  itemRequestService.getItemRequests(userId, from, size);
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(requests));
    }

    @Override
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {

        ItemRequest request = itemRequestService.getItemRequestById(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден запрос с id=%d", requestId)));
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(request));
    }

}
