package ru.practicum.shareit.item.storage;

import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorageCustom {
    List<Item> findAllByExpression(BooleanExpression expression);
}
