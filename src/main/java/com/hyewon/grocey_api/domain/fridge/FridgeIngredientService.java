package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponseDto;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponseDto;
import com.hyewon.grocey_api.global.exception.FridgeIngredientNotFoundException;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeIngredientService {

    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final FridgeRepository fridgeRepository;

    public List<FridgeIngredientResponseDto> getIngredientsByFridge(Long fridgeId, Boolean isFreezer) {
        if (!fridgeRepository.existsById(fridgeId)) {
            throw new FridgeNotFoundException(fridgeId);
        }
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

    public FridgeIngredientDetailResponseDto getIngredientDetail(Long id) {
        FridgeIngredient fi = fridgeIngredientRepository.findById(id)
                .orElseThrow(() -> new FridgeIngredientNotFoundException(id));
        return new FridgeIngredientDetailResponseDto(fi);
    }
}
