package com.hyewon.grocey_api.domain.cart.service;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.repository.CartItemRepository;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(List<Long> cartItemIds) {
        List<CartItem> selectedItems = cartItemRepository.findAllById(cartItemIds);
        if (selectedItems.isEmpty()) {
            throw new InvalidRequestException("No cart items selected.");
        }
        return selectedItems;
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItemsWithProduct(List<Long> cartItemIds, Long userId) {
        List<CartItem> selectedItems = cartItemRepository.findAllByIdInAndUserIdWithProduct(cartItemIds, userId);
        if (selectedItems.isEmpty()) {
            throw new InvalidRequestException("No cart items selected.");
        }
        return selectedItems;
    }

    @Transactional
    public void deleteCartItems(List<CartItem> cartItems) {
        cartItemRepository.deleteAll(cartItems);
    }
}
