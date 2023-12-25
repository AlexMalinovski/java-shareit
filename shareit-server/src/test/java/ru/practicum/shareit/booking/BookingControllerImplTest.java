package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.enums.BookStatus;
import ru.practicum.shareit.booking.enums.StateFilter;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.mapper.EnumMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.library.api.booking.dto.BookingDto;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;
import ru.practicum.shareit.library.api.item.dto.ItemDto;
import ru.practicum.shareit.library.api.item.dto.ItemSimpleDto;
import ru.practicum.shareit.library.api.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingControllerImpl.class)
class BookingControllerImplTest {

    private final long itemId = 1L;
    private final long ownerId = 1L;
    private final long bookerId = 2L;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private final LocalDateTime start = LocalDateTime.of(2100, 1, 1, 0,0,0);
    private final LocalDateTime end = LocalDateTime.of(2100, 1, 2, 0,0,0);

    @MockBean
    private EnumMapper enumMapper;

    @MockBean
    private BookingMapper bookingMapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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

    private CreateBookingDto getCreateBookingDto() {
        return CreateBookingDto.builder()
                .itemId(itemId)
                .start(formatter.format(start))
                .end(formatter.format(end))
                .build();
    }

    private Booking getNewBooking() {
        return Booking.builder()
                .item(getValidItem())
                .booker(getValidBooker())
                .start(start)
                .end(end)
                .build();
    }

    private Booking getBooking() {
        return getNewBooking().toBuilder()
                .status(BookStatus.WAITING)
                .id(1L)
                .build();
    }

    private ItemDto getValidItemDto() {
        return ItemDto.builder()
                .id(itemId)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private ItemSimpleDto getValidItemSimpleDto() {
        return ItemSimpleDto.builder()
                .id(itemId)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private UserDto getValidOwnerDto() {
        return UserDto.builder()
                .id(ownerId)
                .name("owner")
                .email("owner@e.mail")
                .build();
    }

    private UserDto getValidBookerDto() {
        return UserDto.builder()
                .id(bookerId)
                .name("booker")
                .email("booker@e.mail")
                .build();
    }

    private BookingDto getBookingDto() {
        return BookingDto.builder()
                .id(1L)
                .item(getValidItemSimpleDto())
                .booker(getValidBookerDto())
                .status(BookStatus.WAITING.name())
                .start(formatter.format(start))
                .end(formatter.format(end))
                .build();
    }

    @Test
    void createBookingRequest() throws Exception {
        Booking newBooking = getNewBooking();
        when(bookingMapper.mapCreateBookingDtoToBooking(any())).thenReturn(newBooking);
        when(bookingService.createBookingRequest(any(Booking.class))).thenReturn(getBooking());
        when(bookingMapper.mapBookingToBookingDto(any(Booking.class))).thenReturn(getBookingDto());

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", bookerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getCreateBookingDto())))
                .andExpect(status().isOk());

        verify(bookingMapper).mapCreateBookingDtoToBooking(getCreateBookingDto());
        verify(bookingService).createBookingRequest(newBooking.toBuilder()
                .booker(User.builder().id(bookerId).build())
                .build());
        verify(bookingMapper).mapBookingToBookingDto(getBooking());
    }

    @Test
    void approveBooking() throws Exception {
        Booking booking = getBooking();
        when(bookingService.checkOwnerAndApproveBooking(anyLong(), anyLong(), anyBoolean())).thenReturn(booking);
        when(bookingMapper.mapBookingToBookingDto(any(Booking.class))).thenReturn(getBookingDto());

        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", ownerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(bookingService).checkOwnerAndApproveBooking(1L, ownerId, true);
        verify(bookingMapper).mapBookingToBookingDto(booking);
    }

    @Test
    void getBookingById() throws Exception {
        Booking booking = getBooking();
        when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(Optional.of(booking));
        when(bookingMapper.mapBookingToBookingDto(any(Booking.class))).thenReturn(getBookingDto());

        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", ownerId))
                .andExpect(status().isOk());

        verify(bookingService).getBookingById(1L, ownerId);
        verify(bookingMapper).mapBookingToBookingDto(booking);
    }

    @Test
    void getUserBookings() throws Exception {
        Booking booking = getBooking();
        when(enumMapper.mapStringToStateFilter(anyString())).thenReturn(StateFilter.CURRENT);
        when(bookingService.getUserBookings(any(StateFilter.class), anyLong(), anyInt(), anyInt())).thenReturn(List.of(booking));
        when(bookingMapper.mapBookingToBookingDto(anyList())).thenReturn(List.of(getBookingDto()));

        mockMvc.perform(get("/bookings?state=current")
                        .header("X-Sharer-User-Id", bookerId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(enumMapper).mapStringToStateFilter("current");
        verify(bookingService).getUserBookings(StateFilter.CURRENT, bookerId, 0, 10);
        verify(bookingMapper).mapBookingToBookingDto(List.of(booking));
    }

    @Test
    void getOwnerBookings() throws Exception {
        Booking booking = getBooking();
        when(enumMapper.mapStringToStateFilter(anyString())).thenReturn(StateFilter.CURRENT);
        when(bookingService.getOwnerBookings(any(StateFilter.class), anyLong(), anyInt(), anyInt())).thenReturn(List.of(booking));
        when(bookingMapper.mapBookingToBookingDto(anyList())).thenReturn(List.of(getBookingDto()));

        mockMvc.perform(get("/bookings/owner?state=current")
                        .header("X-Sharer-User-Id", ownerId)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());

        verify(enumMapper).mapStringToStateFilter("current");
        verify(bookingService).getOwnerBookings(StateFilter.CURRENT, ownerId, 0, 10);
        verify(bookingMapper).mapBookingToBookingDto(List.of(booking));
    }
}