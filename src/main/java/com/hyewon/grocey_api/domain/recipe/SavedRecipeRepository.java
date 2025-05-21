package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedRecipeRepository extends JpaRepository<SavedRecipe, Long> {
    List<SavedRecipe> findByUser(User user);
    boolean existsByUserAndRecipe(User user, Recipe recipe);
    Optional<SavedRecipe> findByUserAndRecipe(User user, Recipe recipe);

}
