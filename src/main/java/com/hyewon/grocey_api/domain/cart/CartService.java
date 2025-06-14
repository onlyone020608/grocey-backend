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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    @Transactional
    public CartResponseDto getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItemResponseDto> items = cart.getCartItems().stream()
                .map(item -> new CartItemResponseDto(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getProduct().getImageUrl(),
                        item.getProduct().getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toList());

        return new CartResponseDto(cart.getId(), items);
    }

    @Transactional
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

        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItem existingItem = existingItemOpt.get();
            existingItem.updateQuantity(existingItem.getQuantity() + request.getQuantity());
        } else {
            CartItem cartItem = new CartItem(product, request.getQuantity());
            cart.addCartItem(cartItem);
            cartItemRepository.save(cartItem);
        }
    }

    @Transactional
    public void updateCartItemQuantity(Long userId, UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(request.getCartItemId())
                .orElseThrow(() -> new CartItemNotFoundException(request.getCartItemId()));

        if (!cartItem.getCart().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You can only modify your own cart.");
        }

        cartItem.updateQuantity(request.getQuantity());
    }

    @Transactional
    public void deleteCartItems(Long userId, List<Long> cartItemIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> itemsToDelete = cartItemRepository.findAllById(cartItemIds);

        for (CartItem cartItem : itemsToDelete) {
            if (!cartItem.getCart().getId().equals(cart.getId())) {
                throw new AccessDeniedException("Cannot delete cart item not belonging to this user.");
            }
            cart.removeCartItem(cartItem);
        }

        cartItemRepository.deleteAll(itemsToDelete);
    }

    @Transactional
    public void addCartItemsInBatch(Long userId, List<AddCartItemRequest> requests) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(new Cart(user, user.getFridge())));

        List<CartItem> itemsToSave = new ArrayList<>();

        for (AddCartItemRequest request : requests) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException(request.getProductId()));

            Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                    .filter(item -> item.getProduct().getId().equals(product.getId()))
                    .findFirst();

            if (existingItemOpt.isPresent()) {
                CartItem existingItem = existingItemOpt.get();
                existingItem.updateQuantity(existingItem.getQuantity() + request.getQuantity());
            } else {
                CartItem newItem = new CartItem(product, request.getQuantity());
                cart.addCartItem(newItem);
                itemsToSave.add(newItem);
            }
        }

        cartItemRepository.saveAll(itemsToSave);
    }


}
