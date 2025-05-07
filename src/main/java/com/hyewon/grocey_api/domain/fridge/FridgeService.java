package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponseDto;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponseDto;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponseDto;
import com.hyewon.grocey_api.global.exception.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeService {
    private final FridgeRepository fridgeRepository;

    public FridgeResponseDto getFridgeInfo(Long userId) {
        Fridge fridge = fridgeRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Fridge not found"));
        return new FridgeResponseDto(fridge.getFridgeTemperature(), fridge.getFreezerTemperature());
    }


}
