package com.hyewon.grocey_api.domain.order.service;

import com.hyewon.grocey_api.domain.order.repository.OrderRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderCleaner {
    private final OrderRepository orderRepository;

    @Transactional
    public void clean(User user){
        orderRepository.deleteByUser(user);
    }
}
