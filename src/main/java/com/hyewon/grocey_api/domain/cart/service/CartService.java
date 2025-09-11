package com.hyewon.grocey_api.domain.cart.service;

import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.dto.CartItemResponse;
import com.hyewon.grocey_api.domain.cart.dto.CartResponse;
import com.hyewon.grocey_api.domain.cart.dto.UpdateCartItemRequest;
import com.hyewon.grocey_api.domain.cart.entity.Cart;
import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.repository.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.repository.CartRepository;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.product.service.ProductQueryService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.global.exception.CartItemNotFoundException;
import com.hyewon.grocey_api.global.exception.CartNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final UserQueryService userQueryService;
    private final CartItemRepository cartItemRepository;
    private final ProductQueryService productQueryService;

    @Transactional(readOnly = true)
    public CartResponse getCart(Long userId) {
        Cart cart = cartRepository.findByUserIdWithItemsAndProduct(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItemResponse> items = cart.getCartItems().stream()
                .map(CartItemResponse::from)
                .collect(Collectors.toList());

        return new CartResponse(cart.getId(), items);
    }

    @Transactional
    public void addCartItem(Long userId, AddCartItemRequest request) {
        User user = userQueryService.getUserById(userId);

        Product product = productQueryService.getProduct(request.productId());

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.of(user, user.getFridge());
                    return cartRepository.save(newCart);
                });

        cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .ifPresentOrElse(
                        existingItem -> existingItem.updateQuantity(existingItem.getQuantity() + request.quantity()),
                        () -> {
                            CartItem cartItem = CartItem.of(product, request.quantity());
                            cart.addCartItem(cartItem);
                            cartItemRepository.save(cartItem);
                        }
                );
    }

    @Transactional
    public void updateCartItemQuantity(Long userId, UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findByIdAndUserId(request.cartItemId(), userId)
                .orElseThrow(() -> new CartItemNotFoundException(request.cartItemId()));

        cartItem.updateQuantity(request.quantity());
    }

    @Transactional
    public void deleteCartItems(Long userId, List<Long> cartItemIds) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> itemsToDelete = cartItemRepository.findAllByIdInAndUserId(cartItemIds, userId);

        if (itemsToDelete.size() != cartItemIds.size()) {
            throw new AccessDeniedException("Some cart items do not belong to this user.");
        }

        itemsToDelete.forEach(cart::removeCartItem);
    }

    @Transactional
    public void addCartItemsInBatch(Long userId, List<AddCartItemRequest> requests) {
        User user = userQueryService.getUserById(userId);

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.of(user, user.getFridge())));

        for (AddCartItemRequest request : requests) {
            Product product = productQueryService.getProduct(request.productId());
            cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                    .ifPresentOrElse(
                            existingItem -> existingItem.updateQuantity(existingItem.getQuantity() + request.quantity()),
                            () -> {
                                CartItem cartItem = CartItem.of(product, request.quantity());
                                cart.addCartItem(cartItem);
                                cartItemRepository.save(cartItem);
                            }
                    );
        }
    }
}
