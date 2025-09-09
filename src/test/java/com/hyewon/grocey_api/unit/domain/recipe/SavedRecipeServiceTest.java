package com.hyewon.grocey_api.unit.domain.recipe;

import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeResponse;
import com.hyewon.grocey_api.domain.recipe.entity.Recipe;
import com.hyewon.grocey_api.domain.recipe.entity.SavedRecipe;
import com.hyewon.grocey_api.domain.recipe.repository.RecipeRepository;
import com.hyewon.grocey_api.domain.recipe.repository.SavedRecipeRepository;
import com.hyewon.grocey_api.domain.recipe.service.SavedRecipeService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.fixture.RecipeFixture;
import com.hyewon.grocey_api.fixture.UserFixture;
import com.hyewon.grocey_api.global.exception.DuplicateSavedRecipeException;
import com.hyewon.grocey_api.global.exception.SavedRecipeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SavedRecipeServiceTest {
    @Mock private SavedRecipeRepository savedRecipeRepository;
    @Mock private UserQueryService userQueryService;
    @Mock private RecipeRepository recipeRepository;
    @InjectMocks private SavedRecipeService savedRecipeService;

    private User user;
    private Recipe recipe;
    private SavedRecipe savedRecipe;

    @BeforeEach
    void setUp() {
        user = UserFixture.aDefaultUser();
        recipe = RecipeFixture.aRecipe();
        savedRecipe = SavedRecipe.builder()
                .user(user)
                .recipe(recipe)
                .build();
    }

    @Test
    @DisplayName("returns saved recipe list when user exists")
    void shouldReturnSavedRecipes_whenUserExists() {
        // given
        Long userId = 1L;
        given(savedRecipeRepository.findByUserIdWithRecipe(userId)).willReturn(List.of(savedRecipe));

        // when
        List<SavedRecipeResponse> result = savedRecipeService.getSavedRecipes(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipeId()).isEqualTo(10L);
        assertThat(result.get(0).getRecipeName()).isEqualTo("Kimchi Fried Rice");
        assertThat(result.get(0).getImageUrl()).isEqualTo("img.jpg");
    }


    @Test
    @DisplayName("saves new recipe when not already saved")
    void shouldSaveRecipe_whenNotAlreadySaved() {
        // given
        Long userId = 1L;
        given(userQueryService.getUserById(userId)).willReturn(user);
        given(recipeRepository.findById(10L)).willReturn(Optional.of(recipe));
        given(savedRecipeRepository.existsByUserIdAndRecipeId(userId, 10L)).willReturn(false);

        // when
        savedRecipeService.saveRecipe(userId, 10L);

        // then
        verify(savedRecipeRepository).save(any(SavedRecipe.class));
    }

    @Test
    @DisplayName("throws DuplicateSavedRecipeException when recipe is already saved")
    void shouldThrowException_whenRecipeAlreadySaved() {
        // given
        Long userId = 1L;
        given(userQueryService.getUserById(1L)).willReturn(user);
        given(recipeRepository.findById(10L)).willReturn(Optional.of(recipe));
        given(savedRecipeRepository.existsByUserIdAndRecipeId(userId, 10L)).willReturn(true);

        // when & then
        assertThrows(DuplicateSavedRecipeException.class,
                () ->  savedRecipeService.saveRecipe(userId, 10L));
    }

    @Test
    @DisplayName("deletes saved recipe when it exists")
    void shouldDeleteSavedRecipe_whenItExists() {
        // given
        Long userId = 1L;
        given(savedRecipeRepository.findByUserIdAndRecipeId(userId, 10L)).willReturn(Optional.of(savedRecipe));

        // when
        savedRecipeService.deleteRecipe(userId, 10L);

        // then
        verify(savedRecipeRepository).delete(savedRecipe);
    }

    @Test
    @DisplayName("throws SavedRecipeNotFoundException when recipe is not saved")
    void shouldThrowException_whenSavedRecipeNotFound() {
        // given
        Long userId = 1L;
        given(savedRecipeRepository.findByUserIdAndRecipeId(userId, 10L)).willReturn(Optional.empty());

        // when & then
        assertThrows(SavedRecipeNotFoundException.class,
                () ->  savedRecipeService.deleteRecipe(userId, 10L));
    }
}