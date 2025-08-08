package com.hyewon.grocey_api.domain.cart.service;

import com.hyewon.grocey_api.domain.cart.repository.CartRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartCleaner {
    private final CartRepository cartRepository;

    public void clean(User user){
        cartRepository.deleteByUser(user);
    }
}
