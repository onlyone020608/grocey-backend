package com.hyewon.grocey_api.domain.fridge.service;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FridgeService {
    private final UserQueryService userQueryService;

    @Transactional(readOnly = true)
    public FridgeResponse getFridgeInfo(Long userId) {
        User user = userQueryService.getUserById(userId);

        Fridge fridge = user.getFridge();
        if (fridge == null) {
            throw new FridgeNotFoundException(userId);
        }

        return new FridgeResponse(fridge.getFridgeTemperature(), fridge.getFreezerTemperature());
    }
}
