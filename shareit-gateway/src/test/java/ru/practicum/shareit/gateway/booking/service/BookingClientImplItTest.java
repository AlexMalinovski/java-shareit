package ru.practicum.shareit.gateway.booking.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;
import ru.practicum.shareit.library.api.exception.BadRequestParamException;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BookingClientImplItTest {

    @Autowired
    private BookingClient bookingClient;

    @Test
    void createBookingRequest_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                -1L, CreateBookingDto.builder().itemId(1L).start("2023").end("2024").build()));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                0L, CreateBookingDto.builder().itemId(1L).start("2023").end("2024").build()));
    }

    @Test
    void createBookingRequest_ifInvalidDto_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                1L, CreateBookingDto.builder().start("2023").end("2024").build()));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                1L, CreateBookingDto.builder().itemId(-1L).start("2023").end("2024").build()));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                1L, CreateBookingDto.builder().itemId(0L).start("2023").end("2024").build()));

        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                1L, CreateBookingDto.builder().itemId(1L).end("2024").build()));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                1L, CreateBookingDto.builder().itemId(1L).start("").end("2024").build()));

        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                1L, CreateBookingDto.builder().itemId(1L).start("2023").build()));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.createBookingRequest(
                1L, CreateBookingDto.builder().itemId(1L).start("2023").end("").build()));
    }

    @Test
    void approveBooking_ifInvalidOwnerId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.approveBooking(-1L, 1L, true));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.approveBooking(0L, 1L, true));
    }

    @Test
    void approveBooking_ifInvalidBookingId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.approveBooking(1L, -1L, true));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.approveBooking(1L, 0L, true));
    }

    @Test
    void getBookingById_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getBookingById(-1L, 1L));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getBookingById(0L, 1L));
    }

    @Test
    void getBookingById_ifInvalidBookingId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getBookingById(1L, -1L));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getBookingById(1L, 0L));
    }

    @Test
    void getUserBookings_ifInvalidUserId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getUserBookings(
                -1L, "ALL", 0, 1));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getUserBookings(
                0L, "ALL", 0, 1));
    }

    @Test
    void getUserBookings_ifInvalidFrom_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getUserBookings(
                1L, "ALL", -1, 1));
    }

    @Test
    void getUserBookings_ifInvalidSize_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getUserBookings(
                1L, "ALL", 0, -1));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getUserBookings(
                1L, "ALL", 0, 0));
    }

    @Test
    void getUserBookings_ifInvalidState_thenThrowBadRequestParamException() {
        BadRequestParamException ex = assertThrows(BadRequestParamException.class, () -> bookingClient.getUserBookings(
                1L, "NotExistState", 0, 1));
        assertEquals("Unknown state: NotExistState", ex.getMessage());
    }

    @Test
    void getOwnerBookings_ifInvalidOwnerId_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getOwnerBookings(
                -1L, "ALL", 0, 1));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getOwnerBookings(
                0L, "ALL", 0, 1));
    }

    @Test
    void getOwnerBookings_ifInvalidFrom_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getOwnerBookings(
                1L, "ALL", -1, 1));
    }

    @Test
    void getOwnerBookings_ifInvalidSize_thenThrowConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getOwnerBookings(
                1L, "ALL", 0, -1));
        assertThrows(ConstraintViolationException.class, () -> bookingClient.getOwnerBookings(
                1L, "ALL", 0, 0));
    }

    @Test
    void getOwnerBookings_ifInvalidState_thenThrowBadRequestParamException() {
        BadRequestParamException ex = assertThrows(BadRequestParamException.class, () -> bookingClient.getOwnerBookings(
                1L, "NotExistState", 0, 1));
        assertEquals("Unknown state: NotExistState", ex.getMessage());
    }
}