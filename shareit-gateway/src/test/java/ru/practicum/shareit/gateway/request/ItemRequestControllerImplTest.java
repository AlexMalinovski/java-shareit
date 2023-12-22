package ru.practicum.shareit.gateway.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.request.service.ItemRequestClient;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestControllerImpl.class)
class ItemRequestControllerImplTest {

    @MockBean
    private ItemRequestClient itemRequestClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createItemRequest() {
        CreateItemRequestDto expected = CreateItemRequestDto.builder().build();
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(expected)))
                .andExpect(status().isOk());
        verify(itemRequestClient).createItemRequest(1L, expected);
    }

    @Test
    @SneakyThrows
    void getOwnedItemRequests() {
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
        verify(itemRequestClient).getOwnedItemRequests(1L);
    }

    @Test
    @SneakyThrows
    void getItemRequests() {
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());
        verify(itemRequestClient).getItemRequests(1L, 0, 5);
    }

    @Test
    @SneakyThrows
    void getItemRequestById() {
        mockMvc.perform(get("/requests/5")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
        verify(itemRequestClient).getItemRequestById(1L, 5L);
    }
}