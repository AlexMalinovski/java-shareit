package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.CreateItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdateItemDto;
import ru.practicum.shareit.item.model.Item;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemMapperTest {
    private final ItemMapper mapper = new ItemMapperImpl();

    @Test
    void mapCreateItemDtoToItem() {
        CreateItemDto expected = CreateItemDto.builder().name("name").description("descr").available(true).build();

        Item actual = mapper.mapCreateItemDtoToItem(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAvailable(), actual.getAvailable());
        assertNull(actual.getId());
    }

    @Test
    void mapUpdateItemDtoToItem() {
        UpdateItemDto expected = UpdateItemDto.builder().name("name").description("descr").available(true).build();

        Item actual = mapper.mapUpdateItemDtoToItem(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAvailable(), actual.getAvailable());
        assertNull(actual.getId());
    }

    @Test
    void mapItemToItemDto() {
        Item expected = Item.builder().id(1L).name("name").description("descr").available(true).build();

        ItemDto actual = mapper.mapItemToItemDto(expected);

        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getAvailable(), actual.isAvailable());
        assertEquals(expected.getId(), actual.getId());
    }
}