package com.hyewon.grocey_api.domain.fridge.service;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeRepository;
import com.hyewon.grocey_api.domain.user.repository.UserRepository;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FridgeQueryService {
    private final FridgeRepository fridgeRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Fridge getFridge(Long fridgeId) {
        return fridgeRepository.findById(fridgeId).orElseThrow(() -> new FridgeNotFoundException(fridgeId));
    }

    public Fridge getFridgeByUserId(Long userId) {
        return userRepository.findFridgeByUserId(userId)
                .orElseThrow(() -> new FridgeNotFoundException(userId));
    }
}
