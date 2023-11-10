package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    @NonNull
    public Item setOwnerAndCreateItem(@NonNull Item item, long userId) {
        User owner = userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        item.setOwner(owner);
        return itemStorage.createItem(item);
    }

    @Override
    public Optional<Item> getItemById(long id, long requesterId) {
        userStorage.getUserById(requesterId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + id));
        return itemStorage.getItemById(id);
    }

    @Override
    @NonNull
    public Item checkOwnerAndUpdateItem(@NonNull Item itemUpdates, long userId) {
        final Long itemId = Optional.ofNullable(itemUpdates.getId())
                .orElseThrow(() -> new IllegalStateException("Не передан id обновляемой вещи"));
        Item foundItem = itemStorage.getItemById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));

        if (userId != foundItem.getOwner().getId()) {
            throw new NotFoundException(
                    String.format("Пользователь с id=%d не является владельцем вещи с id=%d", userId, itemId));
        }
        return itemStorage.updateItem(foundItem.updateOnNonNullFields(itemUpdates));
    }

    @Override
    @NonNull
    public List<Item> getOwnedItems(long userId) {
        userStorage.getUserById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        return itemStorage.getItemsByOwnerId(userId);
    }

    @Override
    @NonNull
    public List<Item> getAvailableItemsBySubString(@NonNull String text, long requesterId) {
        userStorage.getUserById(requesterId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + requesterId));
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemStorage.getAvailableItemsBySubString(text);
    }
}
