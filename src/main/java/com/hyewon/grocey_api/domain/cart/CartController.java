package com.hyewon.grocey_api.domain.cart;

import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.dto.CartResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.UpdateCartItemRequest;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping
    public CartResponseDto getCart(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return cartService.getCart(userDetails.getId());
    }

    @PostMapping("/items")
    public ResponseEntity<Void> addCartItem(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody AddCartItemRequest request
    ) {
        cartService.addCartItem(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/items")
    public ResponseEntity<Void> updateCartItemQuantity(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItemQuantity(userDetails.getId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/items")
    public ResponseEntity<Void> deleteCartItems(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody List<Long> cartItemIds) {
        cartService.deleteCartItems(userDetails.getId(), cartItemIds);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/items/batch")
    public ResponseEntity<Void> addCartItemsInBatch(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody List<AddCartItemRequest> requests
    ) {
        cartService.addCartItemsInBatch(userDetails.getId(), requests);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }




}
