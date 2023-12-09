package ru.practicum.shareit.item.storage;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QComment;
import ru.practicum.shareit.item.model.QItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ItemStorageCustomImpl implements ItemStorageCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Item> findByCondition(BooleanExpression condition, int from, int size) {
        QItem qItem = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(qItem)
                .where(condition)
                .limit(size)
                .offset(from)
                .fetch();
    }

    @Override
    public List<Item> findByConditionWithCommentsOrder(
            BooleanExpression condition, OrderSpecifier<Long> order, int from, int size) {

        QItem qItem = QItem.item;
        QComment qComment = QComment.comment;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.selectFrom(qItem)
                .leftJoin(qItem.comments, qComment).fetchJoin()
                .leftJoin(qComment.author).fetchJoin()
                .where(condition)
                .orderBy(order)
                .limit(size)
                .offset(from)
                .fetch();
    }
}
