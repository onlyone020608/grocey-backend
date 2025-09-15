package com.hyewon.grocey_api.domain.recommendation.repository;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.recommendation.entity.RecipeRecommendation;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRecommendationRepository extends JpaRepository<RecipeRecommendation, Long> {
    List<RecipeRecommendation> findByUser(User user);
    List<RecipeRecommendation> findByFridge(Fridge fridge);
    void deleteByUser(User user);
}
