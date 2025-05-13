package com.hyewon.grocey_api.domain.recipe;

import com.hyewon.grocey_api.domain.recipe.dto.SavedRecipeDto;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SavedRecipeServiceTest {
    @Mock
    private SavedRecipeRepository savedRecipeRepository;
    @Mock private UserRepository userRepository;
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

}