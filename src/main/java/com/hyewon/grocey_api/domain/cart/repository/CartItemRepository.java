package com.hyewon.grocey_api.domain.cart.repository;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci FROM CartItem ci " +
            "JOIN FETCH ci.cart c " +
            "JOIN FETCH c.user u " +
            "WHERE ci.id = :cartItemId AND u.id = :userId")
    Optional<CartItem> findByIdAndUserId(@Param("cartItemId") Long cartItemId,
                                         @Param("userId") Long userId);
    Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
}
