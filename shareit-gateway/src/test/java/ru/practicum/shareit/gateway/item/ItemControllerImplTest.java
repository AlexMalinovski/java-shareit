package ru.practicum.shareit.gateway.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.gateway.item.service.ItemClient;
import ru.practicum.shareit.library.api.item.dto.CreateCommentDto;
import ru.practicum.shareit.library.api.item.dto.CreateItemDto;
import ru.practicum.shareit.library.api.item.dto.UpdateItemDto;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemControllerImpl.class)
class ItemControllerImplTest {

    @MockBean
    private ItemClient itemClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createItem() {
        CreateItemDto expected = CreateItemDto.builder().build();
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(expected)))
                .andExpect(status().isOk());
        verify(itemClient).createItem(1L, expected);
    }

    @Test
    @SneakyThrows
    void getItemById() {
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk());
        verify(itemClient).getItemById(2L, 1L);
    }

    @Test
    @SneakyThrows
    void updateItem() {
        UpdateItemDto expected = UpdateItemDto.builder().build();
        mockMvc.perform(patch("/items/2")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(expected)))
                .andExpect(status().isOk());
        verify(itemClient).updateItem(1L, 2L, expected);
    }

    @Test
    @SneakyThrows
    void getOwnedItems() {
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
        verify(itemClient).getOwnedItems(1L, 0, 10);
    }

    @Test
    @SneakyThrows
    void searchItems() {
        mockMvc.perform(get("/items/search?text=text")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
        verify(itemClient).searchItems(1L, "text", 0, 10);
    }

    @Test
    @SneakyThrows
    void createComment() {
        CreateCommentDto expected = CreateCommentDto.builder().build();
        mockMvc.perform(post("/items/2/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(expected)))
                .andExpect(status().isOk());
        verify(itemClient).createComment(1L, 2L, expected);
    }
}