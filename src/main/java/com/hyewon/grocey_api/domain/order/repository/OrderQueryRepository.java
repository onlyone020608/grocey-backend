package com.hyewon.grocey_api.domain.order.repository;

import com.hyewon.grocey_api.domain.order.entity.Order;

import java.util.List;

public interface OrderQueryRepository {
    List<Order> findRecentOrders(Long userId);
}
