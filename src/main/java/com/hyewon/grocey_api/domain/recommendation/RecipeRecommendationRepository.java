package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRecommendationRepository extends JpaRepository<RecipeRecommendation, Integer> {
    List<RecipeRecommendation> findByUser(User user);
    List<RecipeRecommendation> findByFridge(Fridge fridge);
    void deleteByUser(User user);


}
