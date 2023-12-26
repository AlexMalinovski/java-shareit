package ru.practicum.shareit.booking.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.library.api.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.library.api.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingMapperTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Mock
    private UserMapper userMapper;

    @Mock
    private ItemMapper itemMapper;

    @InjectMocks
    BookingMapperImpl mapper;

    @Test
    void mapCreateBookingDtoToBooking() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 1, 2, 0, 0, 0);
        CreateBookingDto expected = CreateBookingDto.builder()
                .itemId(1L)
                .start(start.format(FORMATTER))
                .end(end.format(FORMATTER))
                .build();

        var actual = mapper.mapCreateBookingDtoToBooking(expected);

        assertNotNull(actual);
        assertNotNull(actual.getItem());
        assertEquals(BookStatus.WAITING, actual.getStatus());
        assertEquals(expected.getItemId(), actual.getItem().getId());
        assertEquals(expected.getStart(), actual.getStart().format(FORMATTER));
        assertEquals(expected.getEnd(), actual.getEnd().format(FORMATTER));
        assertNull(actual.getBooker());
        assertNull(actual.getId());
    }

    @Test
    void mapCreateBookingDtoToBooking_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapCreateBookingDtoToBooking(null);
        assertNull(actual);
    }

    @Test
    void mapBookingToBookingDto() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 1, 2, 0, 0, 0);
        Booking expected = Booking.builder()
                .id(1L)
                .status(BookStatus.APPROVED)
                .item(Item.builder().build())
                .booker(User.builder().build())
                .start(start)
                .end(end)
                .build();
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(UserDto.builder().build());
        when(itemMapper.mapItemToItemSimpleDto(any(Item.class))).thenReturn(ItemSimpleDto.builder().build());

        var actual = mapper.mapBookingToBookingDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getStatus().name(), actual.getStatus());
        assertEquals(expected.getStart().format(FORMATTER), actual.getStart());
        assertEquals(expected.getEnd().format(FORMATTER), actual.getEnd());
        assertNotNull(actual.getBooker());
        assertNotNull(actual.getItem());
        verify(userMapper).mapUserToUserDto(expected.getBooker());
        verify(itemMapper).mapItemToItemSimpleDto(expected.getItem());
    }

    @Test
    void mapBookingToBookingDto_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapBookingToBookingDto((Booking) null);
        assertNull(actual);
    }

    @Test
    void mapBookingToBookingDto_ifSrcListNull_thenTargetNull() {
        var actual = mapper.mapBookingToBookingDto((List<Booking>) null);
        assertNull(actual);
    }

    @Test
    void mapBookingToBookingDto_List() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 1, 2, 0, 0, 0);
        List<Booking> expected = List.of(Booking.builder()
                .id(1L)
                .status(BookStatus.APPROVED)
                .item(Item.builder().build())
                .booker(User.builder().build())
                .start(start)
                .end(end)
                .build());
        when(userMapper.mapUserToUserDto(any(User.class))).thenReturn(UserDto.builder().build());
        when(itemMapper.mapItemToItemSimpleDto(any(Item.class))).thenReturn(ItemSimpleDto.builder().build());

        var actual = mapper.mapBookingToBookingDto(expected);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(expected.get(0).getId(), actual.get(0).getId());
        assertEquals(expected.get(0).getStatus().name(), actual.get(0).getStatus());
        assertEquals(expected.get(0).getStart().format(FORMATTER), actual.get(0).getStart());
        assertEquals(expected.get(0).getEnd().format(FORMATTER), actual.get(0).getEnd());
        assertNotNull(actual.get(0).getBooker());
        assertNotNull(actual.get(0).getItem());
        verify(userMapper).mapUserToUserDto(expected.get(0).getBooker());
        verify(itemMapper).mapItemToItemSimpleDto(expected.get(0).getItem());
    }

    @Test
    void mapBookingToNewBooking_ifSrcNull_thenTargetNull() {
        var actual = mapper.mapBookingToNewBooking(null);
        assertNull(actual);
    }

    @Test
    void mapBookingToNewBooking() {
        LocalDateTime start = LocalDateTime.of(2022, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2022, 1, 2, 0, 0, 0);
        Booking expected = Booking.builder()
                .id(1L)
                .status(BookStatus.APPROVED)
                .item(Item.builder().id(1L).build())
                .booker(User.builder().id(2L).build())
                .start(start)
                .end(end)
                .build();

        var actual = mapper.mapBookingToNewBooking(expected);
        assertNotNull(actual);
        assertNull(actual.getId());
        assertEquals(BookStatus.WAITING, actual.getStatus());
        assertEquals(expected.getStart(), actual.getStart());
        assertEquals(expected.getEnd(), actual.getEnd());
        assertNotNull(actual.getBooker());
        assertNotNull(actual.getItem());
        assertEquals(expected.getItem(), actual.getItem());
        assertEquals(expected.getBooker(), actual.getBooker());
        assertNotSame(actual.getBooker(), expected.getBooker());
        assertNotSame(actual.getItem(), expected.getItem());
    }
}