package com.hyewon.grocey_api.domain.order;

import com.hyewon.grocey_api.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findTop5ByUserOrderByCreatedAtDesc(User user);
    Page<Order> findByUser(User user, Pageable pageable);
    void deleteByUser(User user);


}
