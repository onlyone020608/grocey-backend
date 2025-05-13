package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeIngredientResponseDto;
import com.hyewon.grocey_api.domain.ingredient.Ingredient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeIngredientServiceTest {
    @Mock
    private FridgeIngredientRepository fridgeIngredientRepository;
    @Mock private FridgeRepository fridgeRepository;

    @InjectMocks
    private FridgeIngredientService fridgeIngredientService;

    @Test
    @DisplayName("getIngredientsByFridge - returns list of ingredients when fridge exists")
    void getIngredientsByFridge_shouldReturnList() {
        // given
        Long fridgeId = 1L;
        Boolean isFreezer = true;

        Ingredient ingredient = new Ingredient("Chicken", "url.com/chicken");
        Fridge fridge = new Fridge(4.0, -18.0);
        FridgeIngredient fi = new FridgeIngredient(fridge, ingredient, true, 2, LocalDate.now());

        given(fridgeRepository.existsById(fridgeId)).willReturn(true);
        given(fridgeIngredientRepository.findByFridgeIdAndIsFreezer(fridgeId, isFreezer))
                .willReturn(singletonList(fi));

        // when
        List<FridgeIngredientResponseDto> result =
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
        Ingredient ingredient = new Ingredient("Milk", "url.com/milk");
        Fridge fridge = new Fridge(4.0, -18.0);
        FridgeIngredient fi = new FridgeIngredient(fridge, ingredient, false, 1, LocalDate.of(2025, 12, 1));

        given(fridgeIngredientRepository.findById(id)).willReturn(java.util.Optional.of(fi));

        // when
        var result = fridgeIngredientService.getIngredientDetail(id);

        // then
        assertThat(result.getIngredientName()).isEqualTo("Milk");
        assertThat(result.getImageUrl()).isEqualTo("url.com/milk");
        assertThat(result.getQuantity()).isEqualTo(1);
        assertThat(result.getIsFreezer()).isFalse();
        assertThat(result.getExpirationDate()).isEqualTo(LocalDate.of(2025, 12, 1));
    }
}