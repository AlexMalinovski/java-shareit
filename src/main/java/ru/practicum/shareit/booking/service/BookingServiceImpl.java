package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.enums.StateFilter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.validator.NewBooking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Validated
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    @Transactional
    public Booking createBookingRequest(@Valid @NewBooking Booking booking) {
        final long bookerId = Optional.of(booking)
                .map(Booking::getBooker)
                .map(User::getId)
                .map(id -> {
                    if (userStorage.existsById(id)) {
                        return id;
                    }
                    throw new NotFoundException(String.format("Не найден пользователь с id=%d.", id));
                })
                .orElseThrow(() -> new IllegalArgumentException("Не указан id пользователя."));

        Item item = Optional.of(booking)
                .map(Booking::getItem)
                .map(Item::getId)
                .map(id -> itemStorage.findById(id)
                        .orElseThrow(() -> new NotFoundException(String.format("Не найдена вещь с id=%d.", id))))
                .orElseThrow(() -> new IllegalArgumentException("Не указан id вещи."));

        if (!item.getAvailable()) {
            throw new BadRequestException(String.format("Вещь с id=%d недоступна для аренды.", item.getId()));
        }
        if (bookerId == item.getOwner().getId()) {
            throw new NotFoundException("Действие недоступно для владельца бронируемой вещи");
        }
        Booking newBooking = booking
                .toBuilder()
                .item(item) // результат, возвращаемый клиенту API, должен включать подробные сведения об item
                .build();

        return bookingStorage.save(newBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(long bookingId, long userId) {
        if (!userStorage.existsById(userId)) {
            throw new NotFoundException(String.format("Не найден пользователь с id=%d.", userId));
        }
        BooleanExpression byBookingId = QBooking.booking.id.eq(bookingId);
        BooleanExpression byBookerIdOrOwnerId = QBooking.booking.booker.id.eq(userId)
                .or(QBooking.booking.item.owner.id.eq(userId));

        return bookingStorage.findAny(byBookingId.and(byBookerIdOrOwnerId));
    }

    @Override
    @Transactional
    public Booking checkOwnerAndApproveBooking(long bookingId, long ownerId, boolean approved) {
        BooleanExpression byBookingId = QBooking.booking.id.eq(bookingId);
        BooleanExpression byOwnerId = QBooking.booking.item.owner.id.eq(ownerId);
        final Booking booking = bookingStorage.findAny(byBookingId.and(byOwnerId))
                .orElseThrow(() -> new NotFoundException(
                        String.format("Не найдено бронирование с id='%d' и ownerId='%d'.", bookingId, ownerId)));
        if (booking.getStatus() != BookStatus.WAITING) {
            throw new BadRequestException(String.format("Статус записи '%s' != '%s'",
                    booking.getStatus().name(), BookStatus.WAITING.name()));
        }
        final Booking updated = booking.toBuilder()
                .status(approved ? BookStatus.APPROVED : BookStatus.REJECTED)
                .build();
        return bookingStorage.save(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getUserBookings(@NonNull StateFilter state, long userId) {
        if (!userStorage.existsById(userId)) {
            // требование ТЗ
            throw new NotFoundException(String.format("Не найден пользователь с id=%d.", userId));
        }
        BooleanExpression byState = state.getExpression(LocalDateTime.now());
        BooleanExpression byBookerId = QBooking.booking.booker.id.eq(userId);
        return bookingStorage.findAllBookingOrderByDateDesc(byBookerId.and(byState));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getOwnerBookings(@NonNull StateFilter state, long ownerId) {
        BooleanExpression byOwnerId = QItem.item.owner.id.eq(ownerId);
        if (itemStorage.count(byOwnerId) == 0) {
            // требование ТЗ
            throw new NotFoundException(String.format("У пользователь с id=%d нет вещей.", ownerId));
        }
        BooleanExpression byState = state.getExpression(LocalDateTime.now());
        byOwnerId = QBooking.booking.item.owner.id.eq(ownerId);
        return bookingStorage.findAllBookingOrderByDateDesc(byOwnerId.and(byState));
    }
}
