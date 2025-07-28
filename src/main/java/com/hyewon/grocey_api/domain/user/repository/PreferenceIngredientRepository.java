package com.hyewon.grocey_api.domain.user.repository;

import com.hyewon.grocey_api.domain.user.entity.PreferenceIngredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceIngredientRepository extends JpaRepository<PreferenceIngredient, Long> {
    boolean existsByName(String name);
}
