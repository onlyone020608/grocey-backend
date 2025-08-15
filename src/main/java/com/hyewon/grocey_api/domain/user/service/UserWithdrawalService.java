package com.hyewon.grocey_api.domain.user.service;

import com.hyewon.grocey_api.domain.cart.service.CartCleaner;
import com.hyewon.grocey_api.domain.order.service.OrderCleaner;
import com.hyewon.grocey_api.domain.recipe.service.SavedRecipeCleaner;
import com.hyewon.grocey_api.domain.recommendation.service.RecommendationCleaner;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWithdrawalService {
    private final UserRepository userRepository;
    private final UserAllergyRepository userAllergyRepository;
    private final UserDislikedIngredientRepository userDislikedIngredientRepository;
    private final UserFoodPreferenceRepository userFoodPreferenceRepository;
    private final UserPreferredIngredientRepository userPreferredIngredientRepository;
    private final SavedRecipeCleaner savedRecipeCleaner;
    private final RecommendationCleaner recipeRecommendationCleaner;
    private final OrderCleaner orderCleaner;
    private final CartCleaner cartCleaner;

    @Transactional
    public void withdraw(User user) {
        userAllergyRepository.deleteByUser(user);
        userDislikedIngredientRepository.deleteByUser(user);
        userFoodPreferenceRepository.deleteByUser(user);
        userPreferredIngredientRepository.deleteByUser(user);

        savedRecipeCleaner.clean(user);
        recipeRecommendationCleaner.clean(user);
        orderCleaner.clean(user);
        cartCleaner.clean(user);

        userRepository.delete(user);
    }
}
