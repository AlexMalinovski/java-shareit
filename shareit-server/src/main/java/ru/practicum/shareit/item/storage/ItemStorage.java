package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@Repository
public interface ItemStorage extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemStorageCustom {

    @EntityGraph("item_with_owner_and_comments_include_author")
    Optional<Item> findOneById(long itemId);

}
