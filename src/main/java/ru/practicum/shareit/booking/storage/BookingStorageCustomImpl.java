package ru.practicum.shareit.booking.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.user.model.QUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class BookingStorageCustomImpl implements BookingStorageCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Booking> findAllBookingOrderByDateDesc(BooleanExpression predicate) {
        QBooking booking = QBooking.booking;
        QUser booker = QUser.user;
        QItem item = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(booking)
                .innerJoin(booking.booker, booker).fetchJoin()
                .innerJoin(booking.item, item).fetchJoin()
                .where(predicate)
                .orderBy(booking.end.desc())
                .fetch();
    }

    @Override
    public Optional<Booking> findAny(BooleanExpression predicate) {
        QBooking booking = QBooking.booking;
        QUser booker = QUser.user;
        QItem item = QItem.item;

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(booking)
                .innerJoin(booking.booker, booker).fetchJoin()
                .innerJoin(booking.item, item).fetchJoin()
                .where(predicate)
                .limit(1L)
                .fetch()
                .stream()
                .findAny();
    }

    @Override
    public Optional<Booking> findTopOrderByTime(BooleanExpression predicate, OrderSpecifier<LocalDateTime> order) {
        QBooking booking = QBooking.booking;
        QUser booker = QUser.user;
        QItem item = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(booking)
                .innerJoin(booking.booker, booker).fetchJoin()
                .innerJoin(booking.item, item).fetchJoin()
                .where(predicate)
                .orderBy(order)
                .limit(1L)
                .fetch()
                .stream()
                .findAny();
    }
}
