package ru.practicum.shareit.item.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorageCustom {
    List<Item> findByCondition(BooleanExpression condition, int from, int size);

    List<Item> findByConditionWithCommentsOrder(BooleanExpression condition, OrderSpecifier<Long> order, int from, int size);

}
