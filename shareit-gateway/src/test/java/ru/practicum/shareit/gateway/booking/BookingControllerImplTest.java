package ru.practicum.shareit.gateway.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.booking.service.BookingClient;
import ru.practicum.shareit.library.api.booking.dto.CreateBookingDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingControllerImpl.class)
class BookingControllerImplTest {

    @MockBean
    private BookingClient bookingClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createBookingRequest() {
        CreateBookingDto expected = CreateBookingDto.builder().build();
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(expected)))
                .andExpect(status().isOk());
        verify(bookingClient).createBookingRequest(1L, expected);
    }

    @Test
    @SneakyThrows
    void approveBooking() {
        mockMvc.perform(patch("/bookings/2?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookingClient).approveBooking(1L, 2L, true);
    }

    @Test
    @SneakyThrows
    void getBookingById() {
        mockMvc.perform(get("/bookings/2")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
        verify(bookingClient).getBookingById(1L, 2L);
    }

    @Test
    @SneakyThrows
    void getUserBookings() {
        mockMvc.perform(get("/bookings?state=current")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
        verify(bookingClient).getUserBookings(1L, "current", 0, 10);
    }

    @Test
    @SneakyThrows
    void getOwnerBookings() {
        mockMvc.perform(get("/bookings/owner?state=current")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
        verify(bookingClient).getOwnerBookings(1L, "current", 0, 10);
    }
}