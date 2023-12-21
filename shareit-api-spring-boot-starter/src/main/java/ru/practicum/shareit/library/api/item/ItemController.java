package ru.practicum.shareit.library.api.item;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

@RequestMapping("/items")
public interface ItemController {
    /**
     * Добавление новой вещи.
     * Эндпойнт POST /items.
     *
     * @param userId        id пользователя, отправившего запрос (владелец вещи)
     * @param createItemDto CreateItemDto
     * @return ItemSimpleDto
     */
    @PostMapping
    ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestBody CreateItemDto createItemDto);

    /**
     * Просмотр информации о конкретной вещи по её идентификатору.
     * Эндпойнт GET /items/{itemId}.
     * Информацию о вещи может просмотреть любой пользователь
     *
     * @param userId id пользователя, отправившего запрос (любой)
     * @param itemId id вещи
     * @return ItemDto
     */
    @GetMapping("/{itemId}")
    ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") long userId,
                                       @PathVariable long itemId);

    /**
     * Редактирование вещи.
     * Эндпойнт PATCH /items/{itemId}.
     * Изменить можно название, описание и статус доступа к аренде.
     * Редактировать вещь может только её владелец.
     *
     * @param userId        id пользователя, отправившего запрос (владелец вещи)
     * @param itemId        id вещи
     * @param updateItemDto UpdateItemDto
     * @return ItemDto
     */
    @PatchMapping("/{itemId}")
    ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable long itemId,
                                      @RequestBody UpdateItemDto updateItemDto);

    /**
     * Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой.
     * Эндпойнт GET /items.
     *
     * @param userId id пользователя, отправившего запрос (владелец вещи)
     * @return List<ItemDto>
     */
    @GetMapping
    ResponseEntity<Object> getOwnedItems(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size);

    /**
     * Поиск вещи потенциальным арендатором.
     * Пользователь передаёт в строке запроса текст, и система ищет вещи, содержащие этот текст в названии или описании.
     * Эндпойнт /items/search?text={text}
     * Возвращает только доступные для аренды вещи.
     *
     * @param userId id пользователя, отправившего запрос (любой)
     * @param text   строка поиска
     * @return List<ItemSimpleDto>
     */
    @GetMapping("/search")
    ResponseEntity<Object> searchItems(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam String text,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size);

    /**
     * Добавление комментария.
     * Эндпоинт POST /items/{itemId}/comment
     *
     * @param userId           id пользователя, бравшего в аренду вещь
     * @param itemId           id вещи
     * @param createCommentDto CreateCommentDto
     * @return CommentDto
     */
    @PostMapping("/{itemId}/comment")
    ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                         @PathVariable long itemId,
                                         @RequestBody CreateCommentDto createCommentDto);
}
