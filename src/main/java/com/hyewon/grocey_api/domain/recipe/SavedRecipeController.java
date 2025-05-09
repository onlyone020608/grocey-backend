package com.hyewon.grocey_api.domain.recipe;


import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SavedRecipeController {

    private final SavedRecipeService savedRecipeService;

    // TODO: Replace {userId} with @AuthenticationPrincipal after implementing JWT
    @GetMapping("/{userId}/recipes")
    public List<SavedRecipeDto> getSavedRecipes(@PathVariable Long userId) {
        return savedRecipeService.getSavedRecipes(userId);
    }
}
