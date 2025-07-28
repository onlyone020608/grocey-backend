package com.hyewon.grocey_api.domain.cart.repository;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
