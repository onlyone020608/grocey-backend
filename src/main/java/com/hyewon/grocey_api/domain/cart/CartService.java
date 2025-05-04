package com.hyewon.grocey_api.domain.cart;

import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
}
