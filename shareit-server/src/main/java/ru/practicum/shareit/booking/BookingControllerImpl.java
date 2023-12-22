package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.enums.StateFilter;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.EnumMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.library.api.booking.BookingController;
import ru.practicum.shareit.library.api.booking.dto.BookingDto;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;
import ru.practicum.shareit.library.api.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookingControllerImpl implements BookingController {

    private final EnumMapper enumMapper;
    private final BookingMapper bookingMapper;
    private final BookingService bookingService;

    @Override
    public ResponseEntity<Object> createBookingRequest(
            @RequestHeader("X-Sharer-User-Id") long userId, @RequestBody CreateBookingDto createBookingDto) {

        Booking booking = bookingMapper.mapCreateBookingDtoToBooking(createBookingDto)
                .toBuilder()
                .booker(User.builder().id(userId).build())
                .build();
        final Booking createdBooking = bookingService.createBookingRequest(booking);
        return ResponseEntity.ok(bookingMapper.mapBookingToBookingDto(createdBooking));
    }

    @Override
    public ResponseEntity<Object> approveBooking(
            @RequestHeader("X-Sharer-User-Id") long ownerId, @PathVariable long bookingId, @RequestParam boolean approved) {

        final Booking booking = bookingService.checkOwnerAndApproveBooking(bookingId, ownerId, approved);
        return ResponseEntity.ok(bookingMapper.mapBookingToBookingDto(booking));
    }

    @Override
    public ResponseEntity<Object> getBookingById(
            @RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {

        final Booking booking = bookingService.getBookingById(bookingId, userId)
                .orElseThrow(() -> new NotFoundException(String.format("Не найдено бронирование с id=%d.", bookingId)));
        return ResponseEntity.ok(bookingMapper.mapBookingToBookingDto(booking));

    }

    @Override
    public ResponseEntity<Object> getUserBookings(
            @RequestHeader("X-Sharer-User-Id") long userId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size) {

        StateFilter stateFilter = enumMapper.mapStringToStateFilter(state);
        List<BookingDto> listDto = bookingMapper.mapBookingToBookingDto(
                bookingService.getUserBookings(stateFilter, userId, from, size));

        return ResponseEntity.ok(listDto);
    }

    @Override
    public ResponseEntity<Object> getOwnerBookings(
            @RequestHeader("X-Sharer-User-Id") long ownerId,
            @RequestParam(required = false, defaultValue = "ALL") String state,
            @RequestParam(required = false, defaultValue = "0") int from,
            @RequestParam(required = false, defaultValue = "20") int size) {

        StateFilter stateFilter = enumMapper.mapStringToStateFilter(state);
        List<BookingDto> listDto = bookingMapper.mapBookingToBookingDto(
                bookingService.getOwnerBookings(stateFilter, ownerId, from, size));

        return ResponseEntity.ok(listDto);
    }
}
