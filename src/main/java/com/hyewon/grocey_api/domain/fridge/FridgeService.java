package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponseDto;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponseDto;
import com.hyewon.grocey_api.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeService {
    private final FridgeRepository fridgeRepository;
    private final FridgeIngredientRepository fridgeIngredientRepository;

    public FridgeResponseDto getFridgeInfo(Long userId) {
        Fridge fridge = fridgeRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Fridge not found"));
        return new FridgeResponseDto(fridge.getFridgeTemperature(), fridge.getFreezerTemperature());
    }

    public List<FridgeIngredientResponseDto> getIngredientsByFridge(Long fridgeId, Boolean isFreezer) {
        List<FridgeIngredient> ingredients;

        if (isFreezer == null) {
            ingredients = fridgeIngredientRepository.findByFridgeId(fridgeId);
        } else {
            ingredients = fridgeIngredientRepository.findByFridgeIdAndIsFreezer(fridgeId, isFreezer);
        }

        return ingredients.stream()
                .map(fi -> new FridgeIngredientResponseDto(
                        fi.getIngredient().getIngredientName(),
                        fi.getIngredient().getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
