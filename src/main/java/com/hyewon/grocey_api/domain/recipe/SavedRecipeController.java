package com.hyewon.grocey_api.domain.recipe;


import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeDto;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/me/recipes")
    public List<SavedRecipeDto> getSavedRecipes(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return savedRecipeService.getSavedRecipes(userDetails.getId());
    }
}
