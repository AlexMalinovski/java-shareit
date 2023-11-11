package ru.practicum.shareit.item.storage;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item createItem(@NonNull Item item);

    Optional<Item> getItemById(final long id);

    Item updateItem(@NonNull Item item);

    List<Item> getItemsByOwnerId(final long userId);

    List<Item> getAvailableItemsBySubString(@NonNull final String text);
}
