package com.hyewon.grocey_api.domain.recommendation.repository;

import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRecommendationRepository extends JpaRepository<RecipeRecommendation, Long> {
    void deleteByUser(User user);
}
