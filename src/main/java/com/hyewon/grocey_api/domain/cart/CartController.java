package com.hyewon.grocey_api.domain.cart;

import com.hyewon.grocey_api.domain.cart.dto.CartItemResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.CartResponseDto;
import lombok.RequiredArgsConstructor;
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

}
