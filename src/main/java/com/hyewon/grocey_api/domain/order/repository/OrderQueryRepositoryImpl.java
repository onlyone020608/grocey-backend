package com.hyewon.grocey_api.domain.order.repository;

import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.QOrder;
import com.hyewon.grocey_api.domain.order.entity.QOrderItem;
import com.hyewon.grocey_api.domain.product.entity.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class OrderQueryRepositoryImpl implements OrderQueryRepository {
    private final JPAQueryFactory query;

    @Override
    public List<Order> findRecentOrders(Long userId) {
        QOrder order = QOrder.order;
        QOrderItem orderItem = QOrderItem.orderItem;
        QProduct product = QProduct.product;
        return query
                .selectFrom(order)
                .distinct()
                .join(order.orderItems, orderItem).fetchJoin()
                .join(orderItem.product, product).fetchJoin()
                .where(order.user.id.eq(userId))
                .orderBy(order.createdAt.desc())
                .limit(5)
                .fetch();
    }
}
