package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.library.api.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceImplItTest {

    @Autowired
    private ItemService itemService;

    @Test
    @Sql("/item-service-it-test.sql")
    void getOwnedItems_ifUserNotFound_thenThrowNotFoundException() {

        assertThrows(NotFoundException.class, () -> itemService.getOwnedItems(10000L, 0, 100));

    }

    @Test
    @Sql("/item-service-it-test.sql")
    void getOwnedItems_ifInvoked_thenReturnOwnedItems() {

        List<Item> actual = itemService.getOwnedItems(1L, 0, 100);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertNotNull(actual.get(0).getLastBooking());
        assertEquals(1, actual.get(0).getLastBooking().getId());
        assertNotNull(actual.get(0).getNextBooking());
        assertEquals(3, actual.get(0).getNextBooking().getId());
        assertNotNull(actual.get(0).getComments());
        assertEquals(1, actual.get(0).getComments().size());
        assertEquals(1, actual.get(0).getComments().get(0).getId());
    }
}