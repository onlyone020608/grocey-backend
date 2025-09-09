package com.hyewon.grocey_api.domain.order.repository;

import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderQueryRepository {
    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.orderItems oi " +
            "JOIN FETCH oi.product " +
            "WHERE o.id = :orderId AND o.user.id = :userId")
    Optional<Order> findByIdAndUserIdWithItemsAndProduct(@Param("orderId") Long orderId,
                                                         @Param("userId") Long userId);
    Page<Order> findByUser(User user, Pageable pageable);
    void deleteByUser(User user);
}
