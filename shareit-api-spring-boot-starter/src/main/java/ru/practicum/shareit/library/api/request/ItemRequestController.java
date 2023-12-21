package ru.practicum.shareit.library.api.request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;

import javax.validation.Valid;

@RequestMapping(path = "/requests")
public interface ItemRequestController {

    /**
     * Добавить новый запрос вещи
     * Эндпойнт - POST /requests
     *
     * @param userId id пользователя
     * @param dto    CreateItemRequestDto
     * @return ItemRequestDto
     */
    @PostMapping
    ResponseEntity<Object> createItemRequest(
            @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody @Valid CreateItemRequestDto dto);

    /**
     * Получить список своих запросов вместе с данными об ответах на них.
     * Эндпойнт - GET /requests
     *
     * @param ownerId id автора запроса
     * @return List<ItemRequestDto>
     */
    @GetMapping
    ResponseEntity<Object> getOwnedItemRequests(@RequestHeader("X-Sharer-User-Id") long ownerId);

    /**
     * Получить список запросов, созданных другими пользователями.
     * Эндпойнт - GET /requests/all
     * С помощью этого эндпоинта пользователи смогут просматривать существующие запросы, на которые они могли бы ответить.
     * Запросы сортируются по дате создания: от более новых к более старым.
     * Результаты должны возвращаться постранично.
     *
     * @param userId id пользователя, запрашивающего данные
     * @param from   пагинация - индекс первого элемента, начиная с 0
     * @param size   пагинация - количество элементов для отображения
     * @return List<ItemRequestDto>
     */
    @GetMapping("/all")
    ResponseEntity<Object> getItemRequests(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size);

    /**
     * Получить данные об одном конкретном запросе вместе с данными об ответах на него
     * Эндпойнт - GET /requests/{requestId}
     *
     * @param userId    id пользователя, запрашивающего данные
     * @param requestId id запроса
     * @return ItemRequestDto
     */
    @GetMapping("/{requestId}")
    ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId);
}
