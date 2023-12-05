package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    /**
     * Добавление новой вещи.
     * Эндпойнт POST /items.
     * @param userId id пользователя, отправившего запрос (владелец вещи)
     * @param createItemDto CreateItemDto
     * @return ItemDto
     */
    @PostMapping
    public ResponseEntity<ItemDto> createItem(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                              @RequestBody @Valid CreateItemDto createItemDto) {
        final Item item = itemMapper.mapCreateItemDtoToItem(createItemDto);
        Item createdItem = itemService.setOwnerAndCreateItem(item, userId);
        return ResponseEntity.ok(itemMapper.mapItemToItemDto(createdItem));
    }

    /**
     * Просмотр информации о конкретной вещи по её идентификатору.
     * Эндпойнт GET /items/{itemId}.
     * Информацию о вещи может просмотреть любой пользователь
     * @param userId id пользователя, отправившего запрос (любой)
     * @param itemId id вещи
     * @return ItemDto
     */
    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                               @PathVariable @Valid @Positive long itemId) {
        Item item = itemService.getItemById(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));
        return ResponseEntity.ok(itemMapper.mapItemToItemDto(item));
    }

    /**
     * Редактирование вещи.
     * Эндпойнт PATCH /items/{itemId}.
     * Изменить можно название, описание и статус доступа к аренде.
     * Редактировать вещь может только её владелец.
     * @param userId id пользователя, отправившего запрос (владелец вещи)
     * @param itemId id вещи
     * @param updateItemDto UpdateItemDto
     * @return ItemDto
     */
    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                              @PathVariable @Valid @Positive long itemId,
                                              @RequestBody UpdateItemDto updateItemDto) {
        final Item item = itemMapper.mapUpdateItemDtoToItem(updateItemDto)
                .toBuilder()
                .id(itemId)
                .build();
        Item updatedItem = itemService.checkOwnerAndUpdateItem(item, userId);
        return ResponseEntity.ok(itemMapper.mapItemToItemDto(updatedItem));
    }

    /**
     * Просмотр владельцем списка всех его вещей с указанием названия и описания для каждой.
     * Эндпойнт GET /items.
     * @param userId id пользователя, отправившего запрос (владелец вещи)
     * @return List<ItemDto>
     */
    @GetMapping
    public ResponseEntity<List<ItemDto>> getOwnedItems(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "20") @Valid @Positive int size) {

        List<ItemDto> items = itemMapper.mapItemToItemDto(
                itemService.getOwnedItems(userId, from, size));
        return ResponseEntity.ok(items);
    }

    /**
     * Поиск вещи потенциальным арендатором.
     * Пользователь передаёт в строке запроса текст, и система ищет вещи, содержащие этот текст в названии или описании.
     * Эндпойнт /items/search?text={text}
     * Возвращает только доступные для аренды вещи.
     * @param userId id пользователя, отправившего запрос (любой)
     * @param text строка поиска
     * @return List<ItemDto>
     */
    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> searchItems(
            @RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId, @RequestParam String text,
            @RequestParam(required = false, defaultValue = "0") @Valid @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "20") @Valid @Positive int size) {

        List<ItemDto> items = itemMapper.mapItemToItemDto(
                itemService.getAvailableItemsBySubString(text, userId, from, size));
        return ResponseEntity.ok(items);
    }

    /**
     * Добавление комментария.
     * Эндпоинт POST /items/{itemId}/comment
     * @param userId id пользователя, бравшего в аренду вещь
     * @param itemId id вещи
     * @param createCommentDto CreateCommentDto
     * @return CommentDto
     */
    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> createComment(@RequestHeader("X-Sharer-User-Id") @Valid @Positive long userId,
                                                    @PathVariable @Valid @Positive long itemId,
                                                    @RequestBody @Valid CreateCommentDto createCommentDto) {
        Comment comment = itemMapper.mapCreateCommentDtoToComment(createCommentDto);
        Comment created = itemService.checkAuthorItemAndCreateComment(userId, itemId, comment);
        return ResponseEntity.ok(itemMapper.mapCommentToCommentDto(created));
    }
}
