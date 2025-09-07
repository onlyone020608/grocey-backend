package com.hyewon.grocey_api.unit.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientDetailResponse;
import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeRepository;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientService;
import com.hyewon.grocey_api.domain.ingredient.entity.Ingredient;
import com.hyewon.grocey_api.global.exception.FridgeIngredientNotFoundException;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
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
    @Mock private FridgeRepository fridgeRepository;
    @InjectMocks private FridgeIngredientService fridgeIngredientService;

    private Fridge fridge;
    private Ingredient ingredient;
    private FridgeIngredient fridgeIngredient1;
    private FridgeIngredient fridgeIngredient2;

    @BeforeEach
    void setUp() {
        fridge = Fridge.builder()
                .id(1L)
                .fridgeTemperature(4.0)
                .freezerTemperature(-18.0)
                .build();
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
    @DisplayName("getIngredientsByFridge - returns list of ingredients when fridge exists")
    void getIngredientsByFridge_shouldReturnList() {
        // given
        Long fridgeId = 1L;
        Boolean isFreezer = true;

        given(fridgeRepository.existsById(fridgeId)).willReturn(true);
        given(fridgeIngredientRepository.findByFridgeIdAndFreezer(fridgeId, isFreezer))
                .willReturn(List.of(fridgeIngredient1));

        // when
        List<FridgeIngredientResponse> result =
                fridgeIngredientService.getIngredientsByFridge(fridgeId, isFreezer);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getIngredientName()).isEqualTo("Chicken");
        assertThat(result.get(0).getImageUrl()).isEqualTo("url.com/chicken");
    }

    @Test
    @DisplayName("getIngredientDetail - returns detailed info of a fridge ingredient")
    void getIngredientDetail_shouldReturnDetailedDto() {
        // given
        Long id = 10L;

        given(fridgeIngredientRepository.findById(id)).willReturn(java.util.Optional.of(fridgeIngredient2));

        // when
        FridgeIngredientDetailResponse result = fridgeIngredientService.getIngredientDetail(id);

        // then
        assertThat(result.getIngredientName()).isEqualTo("Chicken");
        assertThat(result.getImageUrl()).isEqualTo("url.com/chicken");
        assertThat(result.getQuantity()).isEqualTo(1);
        assertThat(result.getIsFreezer()).isFalse();
    }

    @Test
    @DisplayName("getIngredientsByFridge - throws FridgeNotFoundException when fridge does not exist")
    void getIngredientsByFridge_shouldThrowWhenFridgeDoesNotExist() {
        // given
        Long fridgeId = 999L;
        Boolean isFreezer = true;

        given(fridgeRepository.existsById(fridgeId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> fridgeIngredientService.getIngredientsByFridge(fridgeId, isFreezer))
                .isInstanceOf(FridgeNotFoundException.class);
    }

    @Test
    @DisplayName("getIngredientDetail - throws FridgeIngredientNotFoundException when ingredient not found")
    void getIngredientDetail_shouldThrowWhenIngredientNotFound() {
        // given
        Long id = 123L;
        given(fridgeIngredientRepository.findById(id)).willReturn(java.util.Optional.empty());

        // when & then
        assertThatThrownBy(() -> fridgeIngredientService.getIngredientDetail(id))
                .isInstanceOf(FridgeIngredientNotFoundException.class);
    }
}