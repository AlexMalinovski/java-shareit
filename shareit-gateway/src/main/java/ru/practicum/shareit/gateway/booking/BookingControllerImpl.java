package ru.practicum.shareit.gateway.booking;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.booking.service.BookingClient;
import ru.practicum.shareit.library.api.booking.BookingController;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;

@Tag(name = "Бронирования", description = "API для работы с бронированиями")
@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {
    private final BookingClient bookingClient;

    @Override
    @Operation(summary = "Добавление нового запроса на бронирование")
    public ResponseEntity<Object> createBookingRequest(long userId, CreateBookingDto createBookingDto) {
        return bookingClient.createBookingRequest(userId, createBookingDto);
    }

    @Override
    @Operation(summary = "Подтверждение или отклонение запроса на бронирование")
    public ResponseEntity<Object> approveBooking(long ownerId, long bookingId, boolean approved) {
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @Override
    @Operation(summary = "Получение данных о бронировании по id")
    public ResponseEntity<Object> getBookingById(long userId, long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    @Override
    @Operation(summary = "Получение бронирований текущего пользователя")
    public ResponseEntity<Object> getUserBookings(long userId, String state, int from, int size) {
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @Override
    @Operation(summary = "Получение бронирований для всех вещей пользователя")
    public ResponseEntity<Object> getOwnerBookings(long ownerId, String state, int from, int size) {
        return bookingClient.getOwnerBookings(ownerId, state, from, size);
    }
}
