package com.hyewon.grocey_api.domain.fridge.service;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponse;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import com.hyewon.grocey_api.global.exception.FridgeIngredientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FridgeIngredientService {
    private final FridgeIngredientRepository fridgeIngredientRepository;
    private final FridgeQueryService fridgeQueryService;

    @Transactional(readOnly = true)
    public List<FridgeIngredientResponse> getIngredientsByFridge(Long userId, Boolean isFreezer) {
        Fridge fridge = fridgeQueryService.getFridgeByUserId(userId);
        Long fridgeId = fridge.getId();

        List<FridgeIngredient> ingredients;

        if (isFreezer == null) {
            ingredients = fridgeIngredientRepository.findByFridgeIdWithIngredient(fridgeId);
        } else {
            ingredients = fridgeIngredientRepository.findByFridgeIdAndFreezerWithIngredient(fridgeId, isFreezer);
        }

        return ingredients.stream()
                .map(FridgeIngredientResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FridgeIngredientDetailResponse getIngredientDetail(Long id) {
        FridgeIngredient fridgeIngredient = fridgeIngredientRepository.findByIngredientIdWithIngredient(id)
                .orElseThrow(() -> new FridgeIngredientNotFoundException(id));
        return FridgeIngredientDetailResponse.from(fridgeIngredient);
    }
}
