package com.hyewon.grocey_api.domain.cart.repository;

import com.hyewon.grocey_api.domain.cart.entity.Cart;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c " +
            "JOIN FETCH c.cartItems ci " +
            "JOIN FETCH ci.product " +
            "WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithItemsAndProduct(@Param("userId") Long userId);
    Optional<Cart> findByUserId(Long userId);
    void deleteByUser(User user);
}
