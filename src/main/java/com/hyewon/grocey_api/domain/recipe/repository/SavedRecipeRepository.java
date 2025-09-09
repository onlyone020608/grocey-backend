package com.hyewon.grocey_api.domain.recipe.repository;

import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {
    @Query("SELECT sr FROM SavedRecipe sr JOIN FETCH sr.recipe WHERE sr.user.id = :userId")
    List<SavedRecipe> findByUserIdWithRecipe(@Param("userId") Long userId);
    boolean existsByUserAndRecipe(User user, Recipe recipe);
    Optional<SavedRecipe> findByUserIdAndRecipeId(Long userId, Long recipeId);
    boolean existsByUserIdAndRecipeId(Long userId, Long recipeId);
    void deleteByUser(User user);
}
