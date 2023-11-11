package ru.practicum.shareit.item.storage;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.InMemoryStorage;
import ru.practicum.shareit.item.model.Item;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage extends InMemoryStorage<Item> implements ItemStorage {
    @Override
    public Item createItem(@NonNull Item item) {
        return create(item);
    }

    @Override
    public Optional<Item> getItemById(long id) {
        return getById(id);
    }

    @Override
    public Item updateItem(@NonNull Item item) {
        return update(item);
    }

    @Override
    public List<Item> getItemsByOwnerId(final long userId) {
        return objects.values()
                .stream()
                .filter(item -> item.isOwner(userId))
                .sorted(Comparator.comparingLong(Item::getOwnerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getAvailableItemsBySubString(@NonNull final String text) {
        return objects.values()
                .stream()
                .filter(Item::getAvailable)
                .filter(item -> item.isContainSubstring(text))
                .sorted(Comparator.comparingLong(Item::getOwnerId))
                .collect(Collectors.toList());
    }
}
