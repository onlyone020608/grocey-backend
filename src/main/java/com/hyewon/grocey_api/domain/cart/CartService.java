package com.hyewon.grocey_api.domain.cart;

import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.dto.CartItemResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.CartResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.UpdateCartItemRequest;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.product.ProductRepository;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.CartItemNotFoundException;
import com.hyewon.grocey_api.global.exception.CartNotFoundException;
import com.hyewon.grocey_api.global.exception.ProductNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.hyewon.grocey_api.domain.fridge.QFridge.fridge;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartResponseDto getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItemResponseDto> items = cart.getCartItems().stream()
                .map(item -> new CartItemResponseDto(
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new CartResponseDto(cart.getId(), items);
    }

    public void addCartItem(Long userId, AddCartItemRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart(user, user.getFridge());
                    return cartRepository.save(newCart);
                });

        CartItem cartItem = new CartItem(product, request.getQuantity());
        cart.addCartItem(cartItem);
        cartItemRepository.save(cartItem);
    }

    public void updateCartItemQuantity(UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new CartItemNotFoundException(request.getCartItemId()));
        cartItem.updateQuantity(request.getQuantity());
    }
}
