package ru.practicum.shareit.item.storage;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.QItem;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class ItemStorageCustomImpl implements ItemStorageCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Item> findByCondition(BooleanExpression condition, int from, int size) {
        return findBy(QItem.item, condition)
                .limit(size)
                .offset(from)
                .fetch();
    }

    @Override
    public List<Item> findByConditionWithOrder(BooleanExpression condition, OrderSpecifier<Long> order, int from, int size) {
        return findBy(QItem.item, condition)
                .orderBy(order)
                .limit(size)
                .offset(from)
                .fetch();
    }

    private <T> JPAQuery<T> findBy(Expression<T> expression, BooleanExpression condition) {
        QItem qItem = QItem.item;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(expression)
                .from(qItem)
                .where(condition);
    }

}
