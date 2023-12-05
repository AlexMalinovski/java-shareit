package ru.practicum.shareit.request.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRequestStorageCustom {
    List<ItemRequest> findAllFetchItems(BooleanExpression condition, OrderSpecifier<LocalDateTime> order);

    List<ItemRequest> findAllFetchItemsPagination(
            BooleanExpression condition, OrderSpecifier<LocalDateTime> order, int from, int size);

    Optional<ItemRequest> findAnyFetch(BooleanExpression condition);

}
