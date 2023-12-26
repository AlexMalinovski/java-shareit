package ru.practicum.shareit.request.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.QItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class ItemRequestStorageTest {

    @Autowired
    ItemRequestStorage itemRequestStorage;

    @Test
    @Sql({"/item-request-storage-test.sql"})
    void findAllFetchItems_ifInvoked_thenReturnWithConditionAndOrder() {
        BooleanExpression condition = QItemRequest.itemRequest.user.id.eq(2L);
        OrderSpecifier<LocalDateTime> order = QItemRequest.itemRequest.created.desc();

        List<ItemRequest> actual = itemRequestStorage.findAllFetchItems(condition, order);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(ItemRequest.builder().id(1L).build()));
        assertTrue(actual.contains(ItemRequest.builder().id(3L).build()));
        assertEquals(3, actual.get(0).getId());
        assertEquals(1, actual.get(1).getId());
    }

    @Test
    @Sql({"/item-request-storage-test.sql"})
    void findAllFetchItemsPagination_ifInvoked_thenReturnWithConditionAndOrder() {
        BooleanExpression condition = QItemRequest.itemRequest.user.id.eq(2L);
        OrderSpecifier<LocalDateTime> order = QItemRequest.itemRequest.created.desc();

        List<ItemRequest> actual = itemRequestStorage.findAllFetchItemsPagination(condition, order, 0, 100);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(ItemRequest.builder().id(1L).build()));
        assertTrue(actual.contains(ItemRequest.builder().id(3L).build()));
        assertEquals(3, actual.get(0).getId());
        assertEquals(1, actual.get(1).getId());
    }

    @Test
    @Sql({"/item-request-storage-test.sql"})
    void findAllFetchItemsPagination_ifInvoked_thenReturnFromIndex() {
        BooleanExpression condition = QItemRequest.itemRequest.user.id.eq(2L);
        OrderSpecifier<LocalDateTime> order = QItemRequest.itemRequest.created.desc();

        List<ItemRequest> actual = itemRequestStorage.findAllFetchItemsPagination(condition, order, 1, 100);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getId());
    }

    @Test
    @Sql({"/item-request-storage-test.sql"})
    void findAllFetchItemsPagination_ifInvoked_thenReturnListWithMaxSize() {
        BooleanExpression condition = QItemRequest.itemRequest.user.id.eq(2L);
        OrderSpecifier<LocalDateTime> order = QItemRequest.itemRequest.created.desc();

        List<ItemRequest> actual = itemRequestStorage.findAllFetchItemsPagination(condition, order, 0, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(3, actual.get(0).getId());
    }

    @Test
    @Sql({"/item-request-storage-test.sql"})
    void findAnyFetch_ifInvoked_thenReturnWithCondition() {
        BooleanExpression condition = QItemRequest.itemRequest.id.eq(1L);

        Optional<ItemRequest> actual = itemRequestStorage.findAnyFetch(condition);

        assertNotNull(actual);
        assertTrue(actual.isPresent());
        assertEquals(1, actual.get().getId());
    }

    @Test
    @Sql({"/item-request-storage-test.sql"})
    void findAnyFetch_ifNotFound_thenReturnEmptyOptional() {
        BooleanExpression condition = QItemRequest.itemRequest.id.eq(100L);

        Optional<ItemRequest> actual = itemRequestStorage.findAnyFetch(condition);

        assertNotNull(actual);
        assertFalse(actual.isPresent());
    }
}