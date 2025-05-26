package com.hyewon.grocey_api.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodPreferenceRepository extends JpaRepository<FoodPreference, Long> {
    boolean existsByName(String name);
}
