package ru.practicum.shareit.item.service;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    @NonNull
    Item setOwnerAndCreateItem(@NonNull Item item, long userId);

    Optional<Item> getItemById(long id, long requesterId);

    @NonNull
    Item checkOwnerAndUpdateItem(@NonNull Item itemUpdates, long userId);

    @NonNull
    List<Item> getOwnedItems(long userId, int from, int size);

    @NonNull
    List<Item> getAvailableItemsBySubString(@NonNull String text, long requesterId, int from, int size);

    @NonNull
    Comment checkAuthorItemAndCreateComment(long userId, long itemId, @NonNull Comment comment);
}
