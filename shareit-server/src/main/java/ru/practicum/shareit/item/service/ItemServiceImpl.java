package ru.practicum.shareit.item.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.library.api.exception.BadRequestException;
import ru.practicum.shareit.library.api.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final ItemRequestStorage itemRequestStorage;
    private final CommentStorage commentStorage;

    @Override
    @NonNull
    @Transactional()
    public Item setOwnerAndCreateItem(@NonNull Item item, long userId) {
        User owner = userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не найден пользователь с id=" + userId));
        Long requestId = Optional.ofNullable(item.getRequest()).map(ItemRequest::getId).orElse(null);
        if (requestId != null && !itemRequestStorage.existsById(requestId)) {
            throw new NotFoundException(String.format("Запрос с id=%d не существует", requestId));
        }
        return itemStorage.save(item.toBuilder()
                .owner(owner)
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Item> getItemById(final long itemId, final long requesterId) {
        if (!userStorage.existsById(requesterId)) {
            throw new NotFoundException("Не найден пользователь с id=" + requesterId);
        }

        Item foundItem = itemStorage.findOneById(itemId).orElse(null);
        if (foundItem == null || requesterId != foundItem.getOwner().getId()) {
            return Optional.ofNullable(foundItem);
        }

        final LocalDateTime now = LocalDateTime.now();
        final Booking lastBooking = findLastItemBooking(itemId, now).orElse(null);
        final Booking nextBooking = findNextItemBooking(itemId, now).orElse(null);
        return Optional.of(
                foundItem.toBuilder()
                        .lastBooking(lastBooking)
                        .nextBooking(nextBooking)
                        .build());
    }

    @Override
    @NonNull
    @Transactional()
    public Item checkOwnerAndUpdateItem(@NonNull Item itemUpdates, long userId) {
        final Long itemId = itemUpdates.getId();
        Item foundItem = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Не найдена вещь с id=" + itemId));

        if (userId != foundItem.getOwner().getId()) {
            throw new NotFoundException(
                    String.format("Пользователь с id=%d не является владельцем вещи с id=%d", userId, itemId));
        }
        Item updateItem = foundItem.toBuilder()
                .name(itemUpdates.getName() != null ? itemUpdates.getName() : foundItem.getName())
                .description(itemUpdates.getDescription() != null ? itemUpdates.getDescription() : foundItem.getDescription())
                .available(itemUpdates.getAvailable() != null ? itemUpdates.getAvailable() : foundItem.getAvailable())
                .build();
        return itemStorage.save(updateItem);
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Item> getOwnedItems(long userId, int from, int size) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException("Не найден пользователь с id=" + userId);
        }
        final LocalDateTime now = LocalDateTime.now();
        BooleanExpression byOwnerId = QItem.item.owner.id.eq(userId);
        return itemStorage.findByConditionWithCommentsOrder(byOwnerId, QItem.item.id.asc(), from, size)
                .stream()
                .map(item -> item.toBuilder()
                        .lastBooking(findLastItemBooking(item.getId(), now)
                                .orElse(null))
                        .nextBooking(findNextItemBooking(item.getId(), now)
                                .orElse(null))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @NonNull
    @Transactional(readOnly = true)
    public List<Item> getAvailableItemsBySubString(@NonNull String text, long requesterId, int from, int size) {
        if (!userStorage.existsById(requesterId)) {
                throw  new NotFoundException("Не найден пользователь с id=" + requesterId);
        }
        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        BooleanExpression byTextInNameOrDescription = QItem.item.name.containsIgnoreCase(text)
                .or(QItem.item.description.containsIgnoreCase(text));
        BooleanExpression byAvailableTrue = QItem.item.available.isTrue();
        return itemStorage.findByCondition(byAvailableTrue.and(byTextInNameOrDescription), from, size);
    }

    @Override
    @NonNull
    @Transactional
    public Comment checkAuthorItemAndCreateComment(long userId, long itemId, @NonNull Comment comment) {
        BooleanExpression byBookerIdAndItemId = QBooking.booking.booker.id.eq(userId)
                .and(QBooking.booking.item.id.eq(itemId));
        BooleanExpression byStatusApproved = QBooking.booking.status.eq(BookStatus.APPROVED);
        BooleanExpression byEndInPast = QBooking.booking.end.before(LocalDateTime.now());
        Booking booking = bookingStorage
                .findAny(byBookerIdAndItemId
                        .and(byStatusApproved)
                        .and(byEndInPast))
                .orElseThrow(() -> new BadRequestException(
                        String.format("Не найдено бронирование вещи id=%d пользователем с id=%d", itemId, userId)));
        User author = booking.getBooker();
        Item item = booking.getItem();

        return commentStorage.save(comment.toBuilder()
                .author(author)
                .item(item)
                .build());
    }


    private Optional<Booking> findLastItemBooking(final Long itemId, final LocalDateTime onDate) {
        BooleanExpression byItemId = QBooking.booking.item.id.eq(itemId);
        BooleanExpression byStartBefore = QBooking.booking.start.before(onDate);
        BooleanExpression byStatus = QBooking.booking.status.eq(BookStatus.APPROVED);

        return bookingStorage.findTopOrderByTime(
                byItemId.and(byStartBefore).and(byStatus), QBooking.booking.start.desc());
    }

    private Optional<Booking> findNextItemBooking(final Long itemId, final LocalDateTime onDate) {
        BooleanExpression byItemId = QBooking.booking.item.id.eq(itemId);
        BooleanExpression byStartAfter = QBooking.booking.start.after(onDate);
        BooleanExpression byStatus = QBooking.booking.status.eq(BookStatus.APPROVED)
                .or(QBooking.booking.status.eq(BookStatus.WAITING));

        return bookingStorage.findTopOrderByTime(
                byItemId.and(byStartAfter).and(byStatus), QBooking.booking.start.asc());
    }
}
