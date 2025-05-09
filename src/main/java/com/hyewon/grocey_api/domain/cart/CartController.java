package com.hyewon.grocey_api.domain.cart;

import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.dto.CartItemResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.CartResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public CartResponseDto getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT
    @PostMapping("/{userId}/items")
    public ResponseEntity<Void> addCartItem(
            @PathVariable Long userId,
            @RequestBody AddCartItemRequest request
    ) {
        cartService.addCartItem(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
