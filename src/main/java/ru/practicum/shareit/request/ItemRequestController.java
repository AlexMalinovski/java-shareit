package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemRequestMapper itemRequestMapper;


    /**
     * Добавить новый запрос вещи
     * Эндпойнт - POST /requests
     * @param userId id пользователя
     * @param dto CreateItemRequestDto
     * @return ItemRequestDto
     */
    @PostMapping
    public ResponseEntity<ItemRequestDto> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
            @RequestBody @Valid CreateItemRequestDto dto) {

        ItemRequest itemRequest = itemRequestMapper.mapCreateItemRequestDtoToItemRequest(dto);
        ItemRequest created = itemRequestService.createItemRequest(itemRequest, userId);
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(created));
    }

    /**
     * Получить список своих запросов вместе с данными об ответах на них.
     * Эндпойнт - GET /requests
     * @param ownerId id автора запроса
     * @return List<ItemRequestDto>
     */
    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getOwnedItemRequests(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long ownerId) {

        List<ItemRequest> requests = itemRequestService.getOwnedItemRequests(ownerId);
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(requests));
    }

    /**
     * Получить список запросов, созданных другими пользователями.
     * Эндпойнт - GET /requests/all
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания: от более новых к более старым.
     * Результаты должны возвращаться постранично.
     * @param userId id пользователя, запрашивающего данные
     * @param from пагинация - индекс первого элемента, начиная с 0
     * @param size пагинация - количество элементов для отображения
     * @return List<ItemRequestDto>
     */
    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getItemRequests(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "20") @Valid @Positive int size) {

        List<ItemRequest> requests =  itemRequestService.getItemRequests(userId, from, size);
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(requests));
    }

    /**
     * Получить данные об одном конкретном запросе вместе с данными об ответах на него
     * Эндпойнт - GET /requests/{requestId}
     * @param userId id пользователя, запрашивающего данные
     * @param requestId id запроса
     * @return ItemRequestDto
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getItemRequestById(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                                             @PathVariable @Valid @Positive long requestId) {

        ItemRequest request = itemRequestService.getItemRequestById(requestId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найден запрос с id=%d", requestId)));
        return ResponseEntity.ok(itemRequestMapper.mapItemRequestToItemRequestDto(request));
    }

}
