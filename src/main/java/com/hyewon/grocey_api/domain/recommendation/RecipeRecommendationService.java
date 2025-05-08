package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.cart.dto.CartItemResponseDto;
import com.hyewon.grocey_api.domain.cart.dto.CartResponseDto;
import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationDto;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeRecommendationService {

    private final RecipeRecommendationRepository recipeRecommendationRepository;
    private final UserRepository userRepository;


    public List<RecipeRecommendationDto> getRecommendationsByUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<RecipeRecommendation> recommendations = recipeRecommendationRepository.findByUserId(userId);

        if (recommendations.isEmpty()) {
            throw RecommendationNotFoundException.forUserRecipe(userId);
        }

        return recommendations.stream()
                .map(RecipeRecommendationDto::new)
                .toList();

    }
}
