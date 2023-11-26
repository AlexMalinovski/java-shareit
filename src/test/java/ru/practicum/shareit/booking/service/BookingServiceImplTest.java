package ru.practicum.shareit.booking.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.enums.StateFilter;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.QBooking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    private final long itemId = 1L;
    private final long ownerId = 1L;
    private final long bookerId = 2L;
    private final LocalDateTime start = LocalDateTime.of(2100, 1, 1, 0,0,0);
    private final LocalDateTime end = LocalDateTime.of(2100, 1, 2, 0,0,0);

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private ItemStorage itemStorage;

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private Item getValidItem() {
        return Item.builder()
                .id(itemId)
                .name("name")
                .description("description")
                .available(true)
                .owner(getValidOwner())
                .build();
    }

    private User getValidOwner() {
        return User.builder()
                .id(ownerId)
                .name("owner")
                .email("owner@e.mail")
                .build();
    }

    private User getValidBooker() {
        return User.builder()
                .id(bookerId)
                .name("booker")
                .email("booker@e.mail")
                .build();
    }

    private Booking getNewBooking() {
        return Booking.builder()
                .item(Item.builder().id(itemId).build())
                .booker(User.builder().id(bookerId).build())
                .status(BookStatus.WAITING)
                .start(start)
                .end(end)
                .build();
    }

    private Booking getBooking(Long id, BookStatus status) {
        return getNewBooking().toBuilder()
                .item(getValidItem())
                .booker(getValidBooker())
                .status(status)
                .id(id)
                .build();
    }

    @Test
    void createBookingRequest_ifItemNotFound_thenThrowNotFoundException() {
        Booking newBooking = getNewBooking();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.createBookingRequest(newBooking));

        verify(itemStorage).findById(newBooking.getItem().getId());
    }

    @Test
    void createBookingRequest_ifItemNotAvailable_thenThrowBadRequestException() {
        Item itemUnavailable = getValidItem().toBuilder().available(false).build();
        Booking newBooking = getNewBooking().toBuilder().item(itemUnavailable).build();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(itemUnavailable));

        assertThrows(BadRequestException.class, () -> bookingService.createBookingRequest(newBooking));

        verify(itemStorage).findById(newBooking.getItem().getId());
    }

    @Test
    void createBookingRequest_ifBookerNotFound_thenThrowNotFoundException() {
        Booking newBooking = getNewBooking();
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(getValidItem()));
        when(userStorage.findById(newBooking.getBooker().getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> bookingService.createBookingRequest(newBooking));

        verify(itemStorage).findById(newBooking.getItem().getId());
        verify(userStorage).findById(newBooking.getBooker().getId());
    }

    @Test
    void createBookingRequest_saveBooking() {
        Booking newBooking = getNewBooking().toBuilder().build();
        Booking expected = getBooking(null, newBooking.getStatus());
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(getValidItem()));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(getValidBooker()));
        when(bookingStorage.save(any(Booking.class))).thenReturn(expected.toBuilder().id(1L).build());

        bookingService.createBookingRequest(newBooking);

        verify(itemStorage).findById(newBooking.getItem().getId());
        verify(userStorage).findById(newBooking.getBooker().getId());
        verify(bookingStorage).save(expected);
    }

    @Test
    void getBookingById_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 2L));

        verify(userStorage).existsById(2L);
    }

    @Test
    void getBookingById_findByBookingIdAndOwnerOrUserId() {
        final Long bookingId = 1L;
        final Long userId = bookerId;
        BooleanExpression exprcted = QBooking.booking.id.eq(bookingId)
                .and(QBooking.booking.booker.id.eq(userId).or(QBooking.booking.item.owner.id.eq(userId)));
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findAny(any(BooleanExpression.class))).thenReturn(Optional.of(getBooking(bookingId, BookStatus.APPROVED)));

        bookingService.getBookingById(bookingId, userId);

        verify(userStorage).existsById(bookerId);
        verify(bookingStorage).findAny(exprcted);
    }

    @Test
    void checkOwnerAndApproveBooking_ifBookingNotFound_thenThrowNotFoundException() {
        final long bookingIdArg = 1L;
        final long ownerIdArg = 2L;
        final boolean approvedArg = true;
        BooleanExpression expected = QBooking.booking.id.eq(bookingIdArg).and(QBooking.booking.item.owner.id.eq(ownerIdArg));
        when(bookingStorage.findAny(any(BooleanExpression.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bookingService.checkOwnerAndApproveBooking(bookingIdArg, ownerIdArg, approvedArg));

        verify(bookingStorage).findAny(expected);
    }

    @Test
    void checkOwnerAndApproveBooking_ifBookingStatusNotWAITING_thenThrowBadRequestException() {
        Booking booking = getBooking(1L, BookStatus.REJECTED);
        when(bookingStorage.findAny(any(BooleanExpression.class))).thenReturn(Optional.of(booking));

        assertThrows(BadRequestException.class,
                () -> bookingService.checkOwnerAndApproveBooking(1L, ownerId, true));
    }

    @Test
    void checkOwnerAndApproveBooking_ifApproved_saveBookingApproved() {
        Booking booking = getBooking(1L, BookStatus.WAITING);
        Booking expected = booking.toBuilder().status(BookStatus.APPROVED).build();
        when(bookingStorage.findAny(any(BooleanExpression.class))).thenReturn(Optional.of(booking));
        when(bookingStorage.save(any(Booking.class))).thenReturn(expected);

        bookingService.checkOwnerAndApproveBooking(1L, ownerId, true);

        verify(bookingStorage).save(expected);
    }

    @Test
    void checkOwnerAndApproveBooking_ifNotApproved_saveBookingRejected() {
        Booking booking = getBooking(1L, BookStatus.WAITING);
        Booking expected = booking.toBuilder().status(BookStatus.REJECTED).build();
        when(bookingStorage.findAny(any(BooleanExpression.class))).thenReturn(Optional.of(booking));
        when(bookingStorage.save(any(Booking.class))).thenReturn(expected);

        bookingService.checkOwnerAndApproveBooking(1L, ownerId, false);

        verify(bookingStorage).save(expected);
    }

    @Test
    void getUserBookings_ifUserNotFound_thenThrowNotFoundException() {
        when(userStorage.existsById(anyLong())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookingService.getUserBookings(StateFilter.CURRENT, 1L));

        verify(userStorage).existsById(1L);
    }

    @Test
    void getUserBookings() {
        when(userStorage.existsById(anyLong())).thenReturn(true);
        when(bookingStorage.findAllBookingOrderByDateDesc(any())).thenReturn(List.of(getBooking(1L, BookStatus.APPROVED)));

        bookingService.getUserBookings(StateFilter.CURRENT, bookerId);

        verify(bookingStorage).findAllBookingOrderByDateDesc(any());
    }

    @Test
    void getOwnerBookings_ifNoOwnedItems_thenThrowNotFoundException() {
        when(itemStorage.count(any(BooleanExpression.class))).thenReturn(0L);
        BooleanExpression expected = QItem.item.owner.id.eq(1L);

        assertThrows(NotFoundException.class, () -> bookingService.getOwnerBookings(StateFilter.CURRENT, 1L));

        verify(itemStorage).count(expected);
    }

    @Test
    void getOwnerBookings() {
        when(itemStorage.count(any(BooleanExpression.class))).thenReturn(1L);
        when(bookingStorage.findAllBookingOrderByDateDesc(any(BooleanExpression.class)))
                .thenReturn(List.of(getBooking(1L, BookStatus.APPROVED)));

        bookingService.getOwnerBookings(StateFilter.CURRENT, 1L);

        verify(bookingStorage).findAllBookingOrderByDateDesc(any());
    }
}