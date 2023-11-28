package ru.practicum.shareit.item.storage;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;
import ru.practicum.shareit.user.model.QUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ItemStorageCustomImpl implements ItemStorageCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Item> findAllByExpression(BooleanExpression expression) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        QUser owner = QUser.user;
        return queryFactory.selectFrom(qItem)
                .innerJoin(qItem.owner, owner).fetchJoin()
                .where(expression)
                .fetch();
    }
}
