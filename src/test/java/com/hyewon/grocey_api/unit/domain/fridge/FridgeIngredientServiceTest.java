package com.hyewon.grocey_api.unit.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponse;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientService;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.fixture.FridgeFixture;
import com.hyewon.grocey_api.global.exception.FridgeIngredientNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeIngredientServiceTest {
    @Mock private FridgeIngredientRepository fridgeIngredientRepository;
    @Mock private FridgeQueryService fridgeQueryService;
    @InjectMocks private FridgeIngredientService fridgeIngredientService;

    private Fridge fridge;
    private Ingredient ingredient;
    private FridgeIngredient fridgeIngredient1;
    private FridgeIngredient fridgeIngredient2;

    @BeforeEach
    void setUp() {
        fridge = FridgeFixture.aFridge();
        ingredient = Ingredient.builder()
                .name("Chicken")
                .imageUrl("url.com/chicken")
                .build();
        fridgeIngredient1 = FridgeIngredient.builder()
                .fridge(fridge)
                .ingredient(ingredient)
                .freezer(true)
                .quantity(2)
                .build();
        fridgeIngredient2 = FridgeIngredient.builder()
                .fridge(fridge)
                .ingredient(ingredient)
                .freezer(false)
                .quantity(1)
                .build();
    }

    @Test
    @DisplayName("returns list of ingredients when fridge exists")
    void shouldReturnIngredients_whenFridgeExists() {
        // given
        Long userId = 1L;
        Boolean isFreezer = true;

        given(fridgeQueryService.getFridgeByUserId(userId)).willReturn(fridge);
        given(fridgeIngredientRepository.findByFridgeIdAndFreezerWithIngredient(1L, isFreezer))
                .willReturn(List.of(fridgeIngredient1));

        // when
        List<FridgeIngredientResponse> result =
                fridgeIngredientService.getIngredientsByFridge(userId, isFreezer);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).ingredientName()).isEqualTo("Chicken");
        assertThat(result.get(0).imageUrl()).isEqualTo("url.com/chicken");
    }

    @Test
    @DisplayName("returns detailed fridge ingredient info when ingredient exists")
    void shouldReturnIngredientDetail_whenIngredientExists() {
        // given
        Long ingredientId = 10L;

        given(fridgeIngredientRepository.findByIdWithIngredient(ingredientId)).willReturn(java.util.Optional.of(fridgeIngredient2));

        // when
        FridgeIngredientDetailResponse result = fridgeIngredientService.getIngredientDetail(ingredientId);

        // then
        assertThat(result.ingredientName()).isEqualTo("Chicken");
        assertThat(result.imageUrl()).isEqualTo("url.com/chicken");
        assertThat(result.quantity()).isEqualTo(1);
        assertThat(result.isFreezer()).isFalse();
    }

    @Test
    @DisplayName("throws FridgeIngredientNotFoundException when ingredient does not exist")
    void shouldThrowException_whenIngredientNotFound() {
        // given
        Long ingredientId = 123L;
        given(fridgeIngredientRepository.findByIdWithIngredient(ingredientId)).willReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> fridgeIngredientService.getIngredientDetail(ingredientId))
                .isInstanceOf(FridgeIngredientNotFoundException.class);
    }
}