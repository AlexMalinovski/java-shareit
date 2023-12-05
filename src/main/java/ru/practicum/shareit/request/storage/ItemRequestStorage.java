package ru.practicum.shareit.request.storage;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

@Repository
public interface ItemRequestStorage extends PagingAndSortingRepository<ItemRequest, Long>, QuerydslPredicateExecutor<ItemRequest>,
        ItemRequestStorageCustom {
}
