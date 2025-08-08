package com.hyewon.grocey_api.domain.ingredient.service;

import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.domain.ingredient.repository.IngredientRepository;
import com.hyewon.grocey_api.global.exception.IngredientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class IngredientQueryService {
    private final IngredientRepository ingredientRepository;

    @Transactional(readOnly = true)
    public Ingredient getIngredientById(long ingredientId) {
        return ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new IngredientNotFoundException(ingredientId));
    }
}
