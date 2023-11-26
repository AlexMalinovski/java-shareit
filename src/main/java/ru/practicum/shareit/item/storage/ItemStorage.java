package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemStorageCustom {
    List<Item> findByOwner_IdOrderById(Long userId);
}
