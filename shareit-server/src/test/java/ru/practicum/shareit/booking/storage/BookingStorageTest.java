package ru.practicum.shareit.booking.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.storage.BookingStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class BookingStorageTest {

    @Autowired
    private BookingStorage bookingStorage;

    @Test
    @Sql({"/booking-storage-test.sql"})
    void findAllBookingOrderByDateDesc_ifInvoked_thenReturnWithConditionAndOrder() {
        BooleanExpression condition = QBooking.booking.status.eq(BookStatus.APPROVED);

        List<Booking> actual = bookingStorage.findAllBookingOrderByDateDesc(condition, 0, 100);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(3, actual.get(0).getId());
        assertEquals(1, actual.get(1).getId());
    }

    @Test
    @Sql({"/booking-storage-test.sql"})
    void findAllBookingOrderByDateDesc_ifInvoked_thenReturnFromIndex() {
        BooleanExpression condition = QBooking.booking.status.eq(BookStatus.APPROVED);

        List<Booking> actual = bookingStorage.findAllBookingOrderByDateDesc(condition, 1, 100);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getId());
    }

    @Test
    @Sql({"/booking-storage-test.sql"})
    void findAllBookingOrderByDateDesc_ifInvoked_thenReturnListWithMaxSize() {
        BooleanExpression condition = QBooking.booking.status.eq(BookStatus.APPROVED);

        List<Booking> actual = bookingStorage.findAllBookingOrderByDateDesc(condition, 0, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(3, actual.get(0).getId());
    }

    @Test
    @Sql({"/booking-storage-test.sql"})
    void findAny_ifInvoked_thenReturnWithCondition() {
        BooleanExpression condition = QBooking.booking.id.eq(1L);

        Optional<Booking> actual = bookingStorage.findAny(condition);

        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(1, actual.get().getId());
    }

    @Test
    @Sql({"/booking-storage-test.sql"})
    void findAny_ifNotFound_thenReturnEmptyOptional() {
        BooleanExpression condition = QBooking.booking.id.eq(100L);

        Optional<Booking> actual = bookingStorage.findAny(condition);

        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

    @Test
    @Sql({"/booking-storage-test.sql"})
    void findTopOrderByTime_ifInvoked_thenReturnWithConditionAndOrder() {
        BooleanExpression condition = QBooking.booking.status.eq(BookStatus.APPROVED);
        OrderSpecifier<LocalDateTime> order = QBooking.booking.start.desc();

        Optional<Booking> actual = bookingStorage.findTopOrderByTime(condition, order);

        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(3, actual.get().getId());
    }

    @Test
    @Sql({"/booking-storage-test.sql"})
    void findTopOrderByTime_ifNotFound_thenReturnEmptyOptional() {
        BooleanExpression condition = QBooking.booking.status.eq(BookStatus.CANCELED);
        OrderSpecifier<LocalDateTime> order = QBooking.booking.start.desc();

        Optional<Booking> actual = bookingStorage.findTopOrderByTime(condition, order);

        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }

}