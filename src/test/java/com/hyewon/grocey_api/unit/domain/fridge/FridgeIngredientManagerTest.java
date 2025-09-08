package com.hyewon.grocey_api.unit.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.entity.FridgeIngredient;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeIngredientRepository;
import com.hyewon.grocey_api.domain.fridge.service.FridgeIngredientManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FridgeIngredientManagerTest {
    @Mock private FridgeIngredientRepository fridgeIngredientRepository;
    @InjectMocks private FridgeIngredientManager fridgeIngredientManager;

    private FridgeIngredient fridgeIngredient;

    @BeforeEach
    void setUp() {
        fridgeIngredient = FridgeIngredient.builder()
                .id(1L)
                .build();
    }

    @Test
    @DisplayName("getByFridgeId - should return fridge ingredients")
    void getByFridgeId_shouldSucceed() {
        // given
        Long fridgeId = 1L;
        given(fridgeIngredientRepository.findByFridgeId(fridgeId)).willReturn(List.of(fridgeIngredient));

        // when
        List<FridgeIngredient> resultFridgeIngredients = fridgeIngredientManager.getByFridgeId(fridgeId);

        // then
        assertThat(resultFridgeIngredients).isEqualTo(List.of(fridgeIngredient));
    }

    @Test
    @DisplayName("deleteAll - should delete fridge ingredients")
    void deleteAll_shouldSucceed() {
        // given
        List<FridgeIngredient> fridgeIngredients = List.of(fridgeIngredient);

        // when
        fridgeIngredientManager.deleteAll(fridgeIngredients);

        // then
        verify(fridgeIngredientRepository, times(1)).deleteAll(fridgeIngredients);
    }
}
