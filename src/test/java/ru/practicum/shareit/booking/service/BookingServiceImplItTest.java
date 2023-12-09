package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookingServiceImplItTest {

    @Autowired
    BookingService bookingService;

    @Test
    void createBookingRequest_ifBookingStartIsNull_thenThrowConstraintViolationException() {
        User booker = User.builder().id(100L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(null)
                .end(LocalDateTime.of(2100, 1, 2, 0, 0, 0))
                .build();

        assertThrows(ConstraintViolationException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    void createBookingRequest_ifBookingEntIsNull_thenThrowConstraintViolationException() {
        User booker = User.builder().id(100L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(null)
                .build();

        assertThrows(ConstraintViolationException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    void createBookingRequest_ifBookingEndBeforeStart_thenThrowConstraintViolationException() {
        User booker = User.builder().id(100L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(LocalDateTime.of(2099, 1, 1, 0, 0, 0))
                .build();

        assertThrows(ConstraintViolationException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    void createBookingRequest_ifBookingEndEqualStart_thenThrowConstraintViolationException() {
        User booker = User.builder().id(100L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .build();

        assertThrows(ConstraintViolationException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    void createBookingRequest_ifBookingStartBeforeCurrent_thenThrowConstraintViolationException() {
        LocalDateTime now = LocalDateTime.now();
        User booker = User.builder().id(100L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(now.minusDays(1))
                .end(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .build();

        assertThrows(ConstraintViolationException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    @Sql("/booking-service-it-test.sql")
    void createBookingRequest_ifBookerNotFound_thenThrowNotFoundException() {
        User booker = User.builder().id(100L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(LocalDateTime.of(2100, 1, 2, 0, 0, 0))
                .build();

        assertThrows(NotFoundException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    @Sql("/booking-service-it-test.sql")
    void createBookingRequest_ifItemNotFound_thenThrowNotFoundException() {
        User booker = User.builder().id(2L).build();
        Item item = Item.builder().id(1000L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(LocalDateTime.of(2100, 1, 2, 0, 0, 0))
                .build();

        assertThrows(NotFoundException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    @Sql("/booking-service-it-test.sql")
    void createBookingRequest_ifItemNotAvailable_thenThrowBadRequestException() {
        User booker = User.builder().id(2L).build();
        Item item = Item.builder().id(2L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(LocalDateTime.of(2100, 1, 2, 0, 0, 0))
                .build();

        assertThrows(BadRequestException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    @Sql("/booking-service-it-test.sql")
    void createBookingRequest_ifBookerIsOwner_thenThrowNotFoundException() {
        User booker = User.builder().id(1L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(LocalDateTime.of(2100, 1, 2, 0, 0, 0))
                .build();

        assertThrows(NotFoundException.class, () -> bookingService.createBookingRequest(expected));
    }

    @Test
    @Sql("/booking-service-it-test.sql")
    void createBookingRequest_ifInvoked_createNewBooking() {
        User booker = User.builder().id(2L).build();
        Item item = Item.builder().id(1L).build();
        Booking expected = Booking.builder()
                .booker(booker)
                .item(item)
                .status(BookStatus.WAITING)
                .start(LocalDateTime.of(2100, 1, 1, 0, 0, 0))
                .end(LocalDateTime.of(2100, 1, 2, 0, 0, 0))
                .build();

        Booking actual = bookingService.createBookingRequest(expected);

        assertNotNull(actual);
        assertEquals(expected.getBooker(), actual.getBooker());
        assertEquals(expected.getItem(), actual.getItem());
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
        assertEquals(expected.getStatus(), actual.getStatus());

        var saved = bookingService.getBookingById(actual.getId(), 2L);

        assertNotNull(saved);
        assertTrue(saved.isPresent());
        assertEquals(actual, saved.get());
    }
}