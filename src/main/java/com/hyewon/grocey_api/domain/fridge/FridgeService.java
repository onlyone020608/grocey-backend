package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponseDto;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class FridgeService {
    private final FridgeRepository fridgeRepository;

    public FridgeResponseDto getFridgeInfo(Long userId) {
        Fridge fridge = fridgeRepository.findById(userId)
                .orElseThrow(() -> new FridgeNotFoundException(userId));
        return new FridgeResponseDto(fridge.getFridgeTemperature(), fridge.getFreezerTemperature());
    }


}
