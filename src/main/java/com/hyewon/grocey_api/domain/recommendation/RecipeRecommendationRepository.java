package com.hyewon.grocey_api.domain.recommendation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRecommendationRepository extends JpaRepository<RecipeRecommendation, Integer> {
    List<RecipeRecommendation> findByUserId(Long userId);
}
