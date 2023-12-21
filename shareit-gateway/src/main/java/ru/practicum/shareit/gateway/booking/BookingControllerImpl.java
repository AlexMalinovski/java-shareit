package ru.practicum.shareit.gateway.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.gateway.booking.service.BookingClient;
import ru.practicum.shareit.library.api.booking.BookingController;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;

@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {
    private final BookingClient bookingClient;

    @Override
    public ResponseEntity<Object> createBookingRequest(long userId, CreateBookingDto createBookingDto) {
        return bookingClient.createBookingRequest(userId, createBookingDto);
    }

    @Override
    public ResponseEntity<Object> approveBooking(long ownerId, long bookingId, boolean approved) {
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @Override
    public ResponseEntity<Object> getBookingById(long userId, long bookingId) {
        return bookingClient.getBookingById(userId, bookingId);
    }

    @Override
    public ResponseEntity<Object> getUserBookings(long userId, String state, int from, int size) {
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @Override
    public ResponseEntity<Object> getOwnerBookings(long ownerId, String state, int from, int size) {
        return bookingClient.getOwnerBookings(ownerId, state, from, size);
    }
}
