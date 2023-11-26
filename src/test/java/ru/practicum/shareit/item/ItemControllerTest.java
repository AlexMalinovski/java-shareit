package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemMapper itemMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateItemDto getValidCreateItemDto() {
        return CreateItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private Item getValidNewItem() {
        return Item.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private Item getValidItem() {
        return Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .owner(getValidUser())
                .build();
    }

    private User getValidUser() {
        return User.builder()
                .id(1L)
                .name("name")
                .email("name@e.mail")
                .build();
    }

    private ItemDto getValidItemDto() {
        return ItemDto.builder()
                .id(1L)
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private UpdateItemDto getValidUpdateItemDto() {
        return UpdateItemDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();
    }

    private CreateCommentDto getValidCreateCommentDto() {
        return CreateCommentDto.builder().text("comment").build();
    }

    private Comment getValidComment() {
        return getValidNewComment()
                .toBuilder()
                .id(1L)
                .build();
    }

    private Comment getValidNewComment() {
        return Comment.builder()
                .item(getValidItem())
                .author(getValidUser())
                .text("comment")
                .build();
    }

    private CommentDto getValidCommentDto() {
        return CommentDto.builder()
                .id(1L)
                .text("comment")
                .authorName(getValidUser().getName())
                .created("2022-01-01'T'00:00:00")
                .build();
    }

    @Test
    void createItem_isAvailable() throws Exception {
        when(itemMapper.mapCreateItemDtoToItem(any())).thenReturn(getValidNewItem());
        when(itemMapper.mapItemToItemDto(any())).thenReturn(getValidItemDto());
        when(itemService.setOwnerAndCreateItem(any(), anyLong())).thenReturn(getValidItem());

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidCreateItemDto())))
                .andExpect(status().isOk());
    }

    @Test
    void getItemById_isAvailable() throws Exception {
        when(itemMapper.mapItemToItemDto(any())).thenReturn(getValidItemDto());
        when(itemService.getItemById(1L, 1L)).thenReturn(Optional.of(getValidItem()));

        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void updateItem_isAvailable() throws Exception {
        when(itemMapper.mapUpdateItemDtoToItem(any())).thenReturn(getValidNewItem());
        when(itemMapper.mapItemToItemDto(any())).thenReturn(getValidItemDto());
        when(itemService.checkOwnerAndUpdateItem(any(), anyLong())).thenReturn(getValidItem());

        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidUpdateItemDto())))
                .andExpect(status().isOk());
    }

    @Test
    void getOwnedItems_isAvailable() throws Exception {
        when(itemMapper.mapItemToItemDto(any())).thenReturn(getValidItemDto());
        when(itemService.getOwnedItems(1L)).thenReturn(List.of(getValidItem()));

        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void searchItems_isAvailable() throws Exception {
        when(itemMapper.mapItemToItemDto(any())).thenReturn(getValidItemDto());
        when(itemService.getAvailableItemsBySubString(anyString(), anyLong())).thenReturn(List.of(getValidItem()));

        mockMvc.perform(get("/items/search?text=sometext")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void createComment_isAvailable() throws Exception {
        when(itemMapper.mapCreateCommentDtoToComment(any())).thenReturn(getValidNewComment());
        when(itemService.checkAuthorItemAndCreateComment(anyLong(), anyLong(), any(Comment.class)))
                .thenReturn(getValidComment());
        when(itemMapper.mapCommentToCommentDto(any(Comment.class))).thenReturn(getValidCommentDto());

        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(getValidCreateCommentDto())))
                .andExpect(status().isOk());

        verify(itemMapper).mapCreateCommentDtoToComment(getValidCreateCommentDto());
        verify(itemService).checkAuthorItemAndCreateComment(1L, 1L, getValidNewComment());
        verify(itemMapper).mapCommentToCommentDto(getValidComment());
    }

}