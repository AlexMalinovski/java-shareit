package ru.practicum.shareit.booking.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingStorageCustom {
    List<Booking> findAllBookingOrderByDateDesc(BooleanExpression expression, int from, int size);

    Optional<Booking> findAny(BooleanExpression expression);

    Optional<Booking> findTopOrderByTime(BooleanExpression predicate, OrderSpecifier<LocalDateTime> order);
}
