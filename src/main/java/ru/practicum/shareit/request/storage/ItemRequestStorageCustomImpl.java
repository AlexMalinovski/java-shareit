package ru.practicum.shareit.request.storage;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.model.QItemRequest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ItemRequestStorageCustomImpl implements ItemRequestStorageCustom {

    @PersistenceContext
    private EntityManager em;

    private <T> JPAQuery<T> findBy(Expression<T> expression, BooleanExpression condition) {
        QItemRequest itemRequest = QItemRequest.itemRequest;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(expression)
                .from(itemRequest)
                .where(condition);
    }

    private <T> JPAQuery<T> findByFetch(Expression<T> expression, BooleanExpression condition) {
        QItemRequest itemRequest = QItemRequest.itemRequest;
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        return queryFactory.select(expression)
                .from(itemRequest)
                .leftJoin(itemRequest.items).fetchJoin()
                .where(condition);
    }

    @Override
    public List<ItemRequest> findAllFetchItems(BooleanExpression condition, OrderSpecifier<LocalDateTime> order) {
        return findByFetch(QItemRequest.itemRequest, condition).orderBy(order).fetch();
    }

    @Override
    public List<ItemRequest> findAllFetchItemsPagination(BooleanExpression condition,
                                                         OrderSpecifier<LocalDateTime> order, int from, int size) {
//        Long count = query(QItemRequest.itemRequest.count(), condition).fetchOne();
//        Querydsl querydsl = new Querydsl(em, (new PathBuilderFactory()).create(ItemRequest.class));
//        JPQLQuery<ItemRequest> queryPaged = querydsl
//                .applyPagination(pageable, queryFetch(QItemRequest.itemRequest, condition).orderBy(order));
//        return PageableExecutionUtils.getPage(queryPaged.fetch(), pageable, () -> count);

        return findByFetch(QItemRequest.itemRequest, condition)
                .orderBy(order)
                .limit(size)
                .offset(from)
                .fetch();

    }

    @Override
    public Optional<ItemRequest> findAnyFetch(BooleanExpression condition) {
        return findByFetch(QItemRequest.itemRequest, condition)
                .limit(1L)
                .fetch()
                .stream()
                .findAny();
    }
}
