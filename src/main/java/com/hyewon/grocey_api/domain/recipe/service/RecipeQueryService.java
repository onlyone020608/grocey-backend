package com.hyewon.grocey_api.domain.recipe.service;

import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeQueryService {
    private final RecipeRepository recipeRepository;

    @Transactional(readOnly = true)
    public List<Recipe> getRecipes(List<Long> recipeIds){
        return recipeRepository.findAllById(recipeIds);
    }
}
