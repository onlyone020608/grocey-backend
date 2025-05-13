package com.hyewon.grocey_api.domain.recommendation;

import com.hyewon.grocey_api.domain.fridge.Fridge;
import com.hyewon.grocey_api.domain.fridge.FridgeRepository;
import com.hyewon.grocey_api.domain.recipe.Recipe;
import com.hyewon.grocey_api.domain.recommendation.dto.RecipeRecommendationDto;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import com.hyewon.grocey_api.global.exception.RecommendationNotFoundException;
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
class RecipeRecommendationServiceTest {

    @Mock
    private RecipeRecommendationRepository recipeRecommendationRepository;
    @Mock private UserRepository userRepository;
    @Mock private FridgeRepository fridgeRepository;

    @InjectMocks
    private RecipeRecommendationService recipeRecommendationService;

    private User user;
    private Fridge fridge;
    private Recipe recipe;

    @BeforeEach
    void setUp() {
        user = new User("tester", "test@email.com", "pw", AgeGroup.TWENTIES, Gender.FEMALE);
        ReflectionTestUtils.setField(user, "id", 1L);

        fridge = new Fridge(4.0, -18.0);
        ReflectionTestUtils.setField(fridge, "id", 2L);

        recipe = new Recipe("Bibimbap", "step1\nstep2", 20, 2);
        ReflectionTestUtils.setField(recipe, "imageUrl", "bibimbap.jpg");
    }

    @Test
    @DisplayName("getRecommendationsByUser - returns recipe recommendations")
    void getRecommendationsByUser_shouldReturnList() {
        // given
        RecipeRecommendation rr = new RecipeRecommendation(user, recipe, RecommendationType.PREFERENCE_BASED);
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(recipeRecommendationRepository.findByUser(user)).willReturn(List.of(rr));

        // when
        List<RecipeRecommendationDto> result = recipeRecommendationService.getRecommendationsByUser(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipeName()).isEqualTo("Bibimbap");
        assertThat(result.get(0).getRecipeImageUrl()).isEqualTo("bibimbap.jpg");
    }

    @Test
    @DisplayName("getRecommendationsByUser - throws when user not found")
    void getRecommendationsByUser_shouldThrowIfUserNotFound() {
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> recipeRecommendationService.getRecommendationsByUser(1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("getRecommendationsByUser - throws when no recommendations")
    void getRecommendationsByUser_shouldThrowIfEmpty() {
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(recipeRecommendationRepository.findByUser(user)).willReturn(List.of());

        assertThatThrownBy(() -> recipeRecommendationService.getRecommendationsByUser(1L))
                .isInstanceOf(RecommendationNotFoundException.class);
    }

    @Test
    @DisplayName("getRecommendationsByFridge - returns recipe recommendations")
    void getRecommendationsByFridge_shouldReturnList() {
        // given
        RecipeRecommendation rr = new RecipeRecommendation(fridge, recipe, RecommendationType.FRIDGE_BASED);
        given(fridgeRepository.findById(2L)).willReturn(Optional.of(fridge));
        given(recipeRecommendationRepository.findByFridge(fridge)).willReturn(List.of(rr));

        // when
        List<RecipeRecommendationDto> result = recipeRecommendationService.getRecommendationsByFridge(2L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRecipeName()).isEqualTo("Bibimbap");
    }

    @Test
    @DisplayName("getRecommendationsByFridge - throws when fridge not found")
    void getRecommendationsByFridge_shouldThrowIfFridgeNotFound() {
        given(fridgeRepository.findById(2L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> recipeRecommendationService.getRecommendationsByFridge(2L))
                .isInstanceOf(FridgeNotFoundException.class);
    }

    @Test
    @DisplayName("getRecommendationsByFridge - throws when no recommendations")
    void getRecommendationsByFridge_shouldThrowIfEmpty() {
        given(fridgeRepository.findById(2L)).willReturn(Optional.of(fridge));
        given(recipeRecommendationRepository.findByFridge(fridge)).willReturn(List.of());

        assertThatThrownBy(() -> recipeRecommendationService.getRecommendationsByFridge(2L))
                .isInstanceOf(RecommendationNotFoundException.class);
    }





}