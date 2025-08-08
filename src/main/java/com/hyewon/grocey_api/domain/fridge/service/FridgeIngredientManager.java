package com.hyewon.grocey_api.domain.fridge.service;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FridgeIngredientManager {
    private final FridgeIngredientRepository fridgeIngredientRepository;

    @Transactional(readOnly = true)
    public List<FridgeIngredient> getByFridgeId(Long fridgeId) {
        return fridgeIngredientRepository.findByFridgeId(fridgeId);
    }

    @Transactional
    public void deleteAll(List<FridgeIngredient> toRemove) {
        fridgeIngredientRepository.deleteAll(toRemove);
    }

    @Transactional
    public void createFridgeIngredient(FridgeIngredient fridgeIngredient) {
        fridgeIngredientRepository.save(fridgeIngredient);
    }
}
