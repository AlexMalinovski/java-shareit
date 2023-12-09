package ru.practicum.shareit.request.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.QItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;

    @Override
    @NonNull
    @Transactional
    public ItemRequest createItemRequest(@NonNull ItemRequest itemRequest, long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException(String.format("Не найден пользователь с id=%d.", userId));
        }
        ItemRequest newRequest = itemRequest.toBuilder()
                .user(User.builder()
                        .id(userId)
                        .build())
                .build();
        return itemRequestStorage.save(newRequest);
    }

    @Override
    public List<ItemRequest> getOwnedItemRequests(long ownerId) {
        if (!userStorage.existsById(ownerId)) {
            throw new NotFoundException(String.format("Не найден пользователь с id=%d.", ownerId));
        }
        BooleanExpression byRequestAuthor = QItemRequest.itemRequest.user.id.eq(ownerId);
        return itemRequestStorage.findAllFetchItems(byRequestAuthor, QItemRequest.itemRequest.created.desc());
    }

    @Override
    public List<ItemRequest> getItemRequests(long userId, int from, int size) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException(String.format("Не найден пользователь с id=%d.", userId));
        }
        BooleanExpression byNotRequestAuthor = QItemRequest.itemRequest.user.id.eq(userId).not();
        return itemRequestStorage.findAllFetchItemsPagination(
                byNotRequestAuthor, QItemRequest.itemRequest.created.desc(), from, size);
    }

    @Override
    public Optional<ItemRequest> getItemRequestById(long requestId, long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException(String.format("Не найден пользователь с id=%d.", userId));
        }
        BooleanExpression byId = QItemRequest.itemRequest.id.eq(requestId);
        return itemRequestStorage.findAnyFetch(byId);
    }
}
