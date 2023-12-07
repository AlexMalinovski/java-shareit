package ru.practicum.shareit.request.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.item.dto.ItemSimpleDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestMapperTest {

    @Mock
    private ItemMapperImpl itemMapper;

    @InjectMocks
    private ItemRequestMapperImpl mapper;

    @Test
    void mapCreateItemRequestDtoToItemRequest() {
        CreateItemRequestDto expected = CreateItemRequestDto.builder().description("description").build();

        var actual = mapper.mapCreateItemRequestDtoToItemRequest(expected);

        assertNotNull(actual);
        assertEquals(expected.getDescription(), actual.getDescription());
        assertNull(actual.getUser());
        assertNull(actual.getId());
        assertNull(actual.getItems());
        assertNull(actual.getCreated());
    }

    @Test
    void mapItemRequestToItemRequestDto() {
        LocalDateTime dateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0);
        List<Item> items = List.of(Item.builder().build());
        when(itemMapper.mapItemToItemSimpleDto(anyList())).thenReturn(List.of(ItemSimpleDto.builder().build()));
        ItemRequest expected = ItemRequest.builder()
                .id(1L)
                .user(User.builder().build())
                .description("description")
                .items(items)
                .created(dateTime)
                .build();

        var actual = mapper.mapItemRequestToItemRequestDto(expected);

        assertNotNull(expected);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")), actual.getCreated());
        assertNotNull(actual.getItems());
        assertEquals(1, actual.getItems().size());
    }

}