package com.hyewon.grocey_api.domain.recommendation.service;

import com.hyewon.grocey_api.domain.recommendation.repository.RecipeRecommendationRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationCleaner {
    private final RecipeRecommendationRepository recipeRecommendationRepository;

    @Transactional
    public void clean(User user) {
        recipeRecommendationRepository.deleteByUser(user);
    }
}
