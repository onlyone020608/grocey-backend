package com.hyewon.grocey_api.domain.recipe;


import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeDto;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class SavedRecipeController {

    private final SavedRecipeService savedRecipeService;

    @GetMapping("/me/recipes")
    public List<SavedRecipeDto> getSavedRecipes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return savedRecipeService.getSavedRecipes(userDetails.getId());
    }

    @PostMapping("/me/recipes/{recipeId}")
    public ResponseEntity<Void> saveRecipe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long recipeId) {
        savedRecipeService.saveRecipe(userDetails.getId(), recipeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
