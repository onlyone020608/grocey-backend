package com.hyewon.grocey_api.domain.fridge.repository;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long> {
    List<FridgeIngredient> findByFridgeIdAndFreezer(Long fridgeId, Boolean isFreezer);
    List<FridgeIngredient> findByFridgeId(Long fridgeId);
}
