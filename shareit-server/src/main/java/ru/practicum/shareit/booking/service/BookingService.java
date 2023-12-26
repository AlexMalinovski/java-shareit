package ru.practicum.shareit.booking.service;

import org.springframework.lang.NonNull;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.enums.StateFilter;
import ru.practicum.shareit.booking.validator.NewBooking;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking createBookingRequest(@Valid @NewBooking Booking booking);

    Optional<Booking> getBookingById(long bookingId, long userId);

    Booking checkOwnerAndApproveBooking(long bookingId, long ownerId, boolean approved);

    List<Booking> getUserBookings(@NonNull StateFilter state, long userId, int from, int size);

    List<Booking> getOwnerBookings(@NonNull StateFilter state, long ownerId, int from, int size);
}
