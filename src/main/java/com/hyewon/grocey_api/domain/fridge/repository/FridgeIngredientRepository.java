package com.hyewon.grocey_api.domain.fridge.repository;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FridgeIngredientRepository extends JpaRepository<FridgeIngredient, Long> {
    @Query("SELECT fi FROM FridgeIngredient fi " +
            "JOIN FETCH fi.ingredient " +
            "WHERE fi.fridge.id = :fridgeId")
    List<FridgeIngredient> findByFridgeIdWithIngredient(@Param("fridgeId") Long fridgeId);

    @Query("SELECT fi FROM FridgeIngredient fi " +
            "JOIN FETCH fi.ingredient " +
            "WHERE fi.fridge.id = :fridgeId AND fi.freezer = :isFreezer")
    List<FridgeIngredient> findByFridgeIdAndFreezerWithIngredient(@Param("fridgeId") Long fridgeId,
                                                                  @Param("isFreezer")  Boolean isFreezer);
    List<FridgeIngredient> findByFridgeIdAndFreezer(Long fridgeId, Boolean isFreezer);
    List<FridgeIngredient> findByFridgeId(Long fridgeId);
}
