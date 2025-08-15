package com.hyewon.grocey_api.unit.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeRepository;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class FridgeQueryServiceTest {
    @Mock private FridgeRepository fridgeRepository;
    @InjectMocks private FridgeQueryService fridgeQueryService;

    private Fridge fridge;

    @BeforeEach
    void setUp() {
       fridge = Fridge.builder()
               .id(1L)
               .build();
    }

    @Test
    @DisplayName("getFridge - should return fridge")
    void getFridge_shouldSucceed() {
        // given
        Long fridgeId = 1L;
        given(fridgeRepository.findById(fridgeId)).willReturn(
                Optional.of(fridge));

        // when
        Fridge resultFridge = fridgeQueryService.getFridge(fridgeId);

        // then
        assertThat(resultFridge).isEqualTo(fridge);
    }

    @Test
    @DisplayName("getFridge - should throw FridgeNotFoundException when fridge does not exist")
    void getFridge_shouldThrowException_whenFridgeNotFound() {
        // given
        Long fridgeId = 999L;
        given(fridgeRepository.findById(fridgeId)).willReturn(
                Optional.empty());

        // when & then
        assertThrows(FridgeNotFoundException.class,
                () ->  fridgeQueryService.getFridge(fridgeId));
    }
}
