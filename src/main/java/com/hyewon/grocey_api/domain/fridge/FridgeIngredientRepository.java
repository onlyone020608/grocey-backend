package com.hyewon.grocey_api.domain.fridge;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long> {
    List<FridgeIngredient> findByFridgeIdAndIsFreezer(Long fridgeId, Boolean isFreezer);
    List<FridgeIngredient> findByFridgeId(Long fridgeId);
}
