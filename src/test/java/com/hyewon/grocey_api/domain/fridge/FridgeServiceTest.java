package com.hyewon.grocey_api.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponseDto;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeServiceTest {
    @Mock
    private FridgeRepository fridgeRepository;

    @InjectMocks
    private FridgeService fridgeService;

    @Test
    @DisplayName("getFridgeInfo - returns fridge temperature info successfully")
    void getFridgeInfo_shouldReturnTemperature() {
        // given
        Long userId = 1L;
        Fridge fridge = new Fridge(4.0, -18.0);

        given(fridgeRepository.findById(userId)).willReturn(Optional.of(fridge));

        // when
        FridgeResponseDto response = fridgeService.getFridgeInfo(userId);

        // then
        assertThat(response.getFridgeTemperature()).isEqualTo(4.0);
        assertThat(response.getFreezerTemperature()).isEqualTo(-18.0);
    }

    @Test
    @DisplayName("getFridgeInfo - throws FridgeNotFoundException when fridge is not found")
    void getFridgeInfo_shouldThrowWhenFridgeNotFound() {
        // given
        Long userId = 999L;
        given(fridgeRepository.findById(userId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> fridgeService.getFridgeInfo(userId))
                .isInstanceOf(FridgeNotFoundException.class);
    }


}