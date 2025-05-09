package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeDto;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SavedRecipeService {
    private final SavedRecipeRepository savedRecipeRepository;
    private final UserRepository userRepository;

    public List<SavedRecipeDto> getSavedRecipes(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<SavedRecipe> savedRecipes = savedRecipeRepository.findByUser(user);

        return savedRecipes.stream()
                .map(SavedRecipeDto::new)
                .toList();

    }
}
