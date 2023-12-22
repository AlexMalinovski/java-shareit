package ru.practicum.shareit.gateway.booking.service;

import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

public interface BookingClient {

    ResponseEntity<Object> createBookingRequest(@Valid @Positive long userId, @Valid CreateBookingDto createBookingDto);

    ResponseEntity<Object> approveBooking(
            @Valid @Positive long ownerId, @Valid @Positive long bookingId, boolean approved);

    ResponseEntity<Object> getBookingById(@Valid @Positive long userId, @Valid @Positive long bookingId);

    ResponseEntity<Object> getUserBookings(
            @Valid @Positive long userId, String state, @Valid @PositiveOrZero int from, @Valid @Positive int size);

    ResponseEntity<Object> getOwnerBookings(
            @Valid @Positive long ownerId, String state, @Valid @PositiveOrZero int from, @Valid @Positive int size);
}
