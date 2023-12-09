package ru.practicum.shareit.item.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=validate"
})
class ItemStorageTest {

    @Autowired
    private ItemStorage itemStorage;

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByCondition_ifInvoked_thenReturnWithCondition() {
        BooleanExpression condition = QItem.item.available.isTrue();

        List<Item> actual = itemStorage.findByCondition(condition, 0, 100);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(Item.builder().id(1L).build()));
        assertTrue(actual.contains(Item.builder().id(3L).build()));
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByCondition_ifInvoked_thenReturnSortedByIdAsc() {
        BooleanExpression condition = QItem.item.available.isTrue();

        List<Item> actual = itemStorage.findByCondition(condition, 0, 100);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(1, actual.get(0).getId());
        assertEquals(3, actual.get(1).getId());
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByCondition_ifInvoked_thenReturnFromIndex() {
        BooleanExpression condition = QItem.item.available.isTrue();

        List<Item> actual = itemStorage.findByCondition(condition, 1, 100);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(3, actual.get(0).getId());
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByCondition_ifInvoked_thenReturnListWithMaxSize() {
        BooleanExpression condition = QItem.item.available.isTrue();

        List<Item> actual = itemStorage.findByCondition(condition, 0, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getId());
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByConditionWithCommentsOrder_ifInvoked_thenReturnWithCondition() {
        BooleanExpression condition = QItem.item.available.isTrue();
        OrderSpecifier<Long> order = QItem.item.id.desc();

        List<Item> actual = itemStorage.findByConditionWithCommentsOrder(condition, order, 0, 100);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertTrue(actual.contains(Item.builder().id(1L).build()));
        assertTrue(actual.contains(Item.builder().id(3L).build()));
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByConditionWithCommentsOrder_ifInvoked_thenReturnSortedByOrder() {
        BooleanExpression condition = QItem.item.available.isTrue();
        OrderSpecifier<Long> order = QItem.item.id.desc();

        List<Item> actual = itemStorage.findByConditionWithCommentsOrder(condition, order, 0, 100);

        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertEquals(3, actual.get(0).getId());
        assertEquals(1, actual.get(1).getId());
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByConditionWithCommentsOrder_ifInvoked_thenReturnFromIndex() {
        BooleanExpression condition = QItem.item.available.isTrue();
        OrderSpecifier<Long> order = QItem.item.id.asc();

        List<Item> actual = itemStorage.findByConditionWithCommentsOrder(condition, order, 1, 100);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(3, actual.get(0).getId());
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByConditionWithCommentsOrder_ifInvoked_thenReturnListWithMaxSize() {
        BooleanExpression condition = QItem.item.available.isTrue();
        OrderSpecifier<Long> order = QItem.item.id.asc();

        List<Item> actual = itemStorage.findByConditionWithCommentsOrder(condition, order, 0, 1);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(1, actual.get(0).getId());
    }

    @Test
    @Sql({"/item-storage-test.sql"})
    void findByConditionWithCommentsOrder_ifInvoked_thenReturnWithComments() {
        BooleanExpression condition = QItem.item.available.isTrue();
        OrderSpecifier<Long> order = QItem.item.id.asc();

        Item actual = itemStorage.findByConditionWithCommentsOrder(condition, order, 0, 1)
                .get(0)
                .toBuilder()
                .build();

        assertNotNull(actual.getComments());
        assertEquals(1, actual.getComments().size());
        assertEquals(1, actual.getComments().get(0).getId());
    }
}