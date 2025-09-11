package com.hyewon.grocey_api.unit.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeQueryService;
import com.hyewon.grocey_api.domain.fridge.service.FridgeService;
import com.hyewon.grocey_api.fixture.FridgeFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeServiceTest {
    @Mock private FridgeQueryService fridgeQueryService;
    @InjectMocks private FridgeService fridgeService;

    private Fridge fridge;

    @BeforeEach
    void setUp() {
        fridge = FridgeFixture.aFridge();
    }

    @Test
    @DisplayName("returns fridge temperature info when user exists")
    void shouldReturnFridgeTemperatureInfo_whenUserExists() {
        // given
        Long userId = 1L;

        given(fridgeQueryService.getFridgeByUserId(userId)).willReturn(fridge);
        // when
        FridgeResponse response = fridgeService.getFridgeInfo(userId);

        // then
        assertThat(response.fridgeTemperature()).isEqualTo(4.0);
        assertThat(response.freezerTemperature()).isEqualTo(-18.0);
    }
}