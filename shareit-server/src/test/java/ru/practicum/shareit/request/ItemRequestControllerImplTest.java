package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.library.api.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestControllerImpl.class)
class ItemRequestControllerImplTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @MockBean
    private ItemRequestMapper itemRequestMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @SneakyThrows
    void createItemRequest() {
        CreateItemRequestDto dto = CreateItemRequestDto.builder()
                .description("description")
                .build();
        ItemRequest obj = ItemRequest.builder()
                .description("description")
                .build();
        ItemRequest createdObj = obj.toBuilder()
                .id(1L)
                .build();
        when(itemRequestMapper.mapCreateItemRequestDtoToItemRequest(any())).thenReturn(obj);
        when(itemRequestService.createItemRequest(any(ItemRequest.class), anyLong())).thenReturn(createdObj);

        mockMvc.perform(post("/requests")
                    .header("X-Sharer-User-Id", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk());

        verify(itemRequestMapper).mapCreateItemRequestDtoToItemRequest(dto);
        verify(itemRequestService).createItemRequest(obj, 1L);
        verify(itemRequestMapper).mapItemRequestToItemRequestDto(createdObj);
    }

    @Test
    @SneakyThrows
    void getOwnedItemRequests() {
        List<ItemRequest> items = new ArrayList<>();
        when(itemRequestService.getOwnedItemRequests(anyLong())).thenReturn(items);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemRequestService).getOwnedItemRequests(1L);
        verify(itemRequestMapper).mapItemRequestToItemRequestDto(items);
    }

    @Test
    @SneakyThrows
    void getItemRequests() {
        List<ItemRequest> items = new ArrayList<>();
        when(itemRequestService.getItemRequests(anyLong(), anyInt(), anyInt())).thenReturn(items);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());

        verify(itemRequestService).getItemRequests(1L, 0, 5);
        verify(itemRequestMapper).mapItemRequestToItemRequestDto(items);
    }

    @Test
    @SneakyThrows
    void getItemRequestById() {
        ItemRequest request = ItemRequest.builder().build();
        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(Optional.of(request));

        mockMvc.perform(get("/requests/5")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());

        verify(itemRequestService).getItemRequestById(5L, 1L);
        verify(itemRequestMapper).mapItemRequestToItemRequestDto(request);
    }

    @Test
    @SneakyThrows
    void getItemRequestById_ifItemNotFount_thenStatus404() {
        when(itemRequestService.getItemRequestById(anyLong(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/requests/5")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isNotFound());

        verify(itemRequestService).getItemRequestById(5L, 1L);
    }
}