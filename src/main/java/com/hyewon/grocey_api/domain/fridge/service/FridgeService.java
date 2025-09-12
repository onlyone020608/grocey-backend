package com.hyewon.grocey_api.domain.fridge.service;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FridgeService {
    private final FridgeQueryService fridgeQueryService;

    @Transactional(readOnly = true)
    @Cacheable(value = "fridge", key = "#userId")
    public FridgeResponse getFridgeInfo(Long userId) {
        Fridge fridge = fridgeQueryService.getFridgeByUserId(userId);

        return FridgeResponse.from(fridge);
    }
}
