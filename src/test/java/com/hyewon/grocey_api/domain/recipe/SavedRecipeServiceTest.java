package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeDto;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.DuplicateSavedRecipeException;
import com.hyewon.grocey_api.global.exception.SavedRecipeNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SavedRecipeServiceTest {
    @Mock
    private SavedRecipeRepository savedRecipeRepository;
    @Mock private UserRepository userRepository;
    @Mock private RecipeRepository recipeRepository;
    @InjectMocks
    private SavedRecipeService savedRecipeService;

    private User user;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        user = new User("tester", "test@email.com", "pw", AgeGroup.TWENTIES, Gender.FEMALE);
        ReflectionTestUtils.setField(user, "id", 1L);

        recipe = new Recipe("Kimchi Fried Rice", "step1\nstep2", 15, 2);
        ReflectionTestUtils.setField(recipe, "id", 10L);
        ReflectionTestUtils.setField(recipe, "imageUrl", "img.jpg");
    }

    @Test
    @DisplayName("getSavedRecipes - returns saved recipe list for user")
    void getSavedRecipes_shouldReturnList() {
        // given
        SavedRecipe savedRecipe = new SavedRecipe(user, recipe);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(savedRecipeRepository.findByUser(user)).willReturn(List.of(savedRecipe));

        // when
        List<SavedRecipeDto> result = savedRecipeService.getSavedRecipes(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipeId()).isEqualTo(10L);
        assertThat(result.get(0).getRecipeName()).isEqualTo("Kimchi Fried Rice");
        assertThat(result.get(0).getImageUrl()).isEqualTo("img.jpg");
    }

    @Test
    @DisplayName("getSavedRecipes - throws UserNotFoundException when user does not exist")
    void getSavedRecipes_shouldThrowIfUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> savedRecipeService.getSavedRecipes(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("saveRecipe - saves new recipe when not already saved")
    void saveRecipe_shouldSaveRecipeIfNotExists() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(recipeRepository.findById(10L)).willReturn(Optional.of(recipe));
        given(savedRecipeRepository.existsByUserAndRecipe(user, recipe)).willReturn(false);

        // when
        savedRecipeService.saveRecipe(1L, 10L);

        // then
        verify(savedRecipeRepository).save(any(SavedRecipe.class));
    }

    @Test
    @DisplayName("saveRecipe - throws DuplicateSavedRecipeException when recipe already saved")
    void saveRecipe_shouldThrowIfRecipeAlreadySaved() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(recipeRepository.findById(10L)).willReturn(Optional.of(recipe));
        given(savedRecipeRepository.existsByUserAndRecipe(user, recipe)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> savedRecipeService.saveRecipe(1L, 10L))
                .isInstanceOf(DuplicateSavedRecipeException.class)
                .hasMessageContaining("Recipe already saved");
    }

    @Test
    @DisplayName("deleteRecipe - deletes saved recipe if exists")
    void deleteRecipe_shouldDeleteIfExists() {
        // given
        SavedRecipe savedRecipe = new SavedRecipe(user, recipe);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(recipeRepository.findById(10L)).willReturn(Optional.of(recipe));
        given(savedRecipeRepository.findByUserAndRecipe(user, recipe)).willReturn(Optional.of(savedRecipe));

        // when
        savedRecipeService.deleteRecipe(1L, 10L);

        // then
        verify(savedRecipeRepository).delete(savedRecipe);
    }

    @Test
    @DisplayName("deleteRecipe - throws SavedRecipeNotFoundException if recipe not saved")
    void deleteRecipe_shouldThrowIfNotSaved() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(recipeRepository.findById(10L)).willReturn(Optional.of(recipe));
        given(savedRecipeRepository.findByUserAndRecipe(user, recipe)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> savedRecipeService.deleteRecipe(1L, 10L))
                .isInstanceOf(SavedRecipeNotFoundException.class)
                .hasMessageContaining("Saved recipe not found");
    }



}