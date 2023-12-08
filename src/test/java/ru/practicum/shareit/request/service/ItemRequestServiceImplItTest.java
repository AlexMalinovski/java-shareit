package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemRequestServiceImplItTest {

    @Autowired
    private ItemRequestService itemRequestService;

    @Test
    @Sql("/item-request-service-it-test.sql")
    void getItemRequests() {
        var actual = itemRequestService.getItemRequests(1L, 0, 100);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(3, actual.get(0).getId());
        assertEquals(1, actual.get(1).getId());
    }
}