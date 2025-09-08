package com.hyewon.grocey_api.unit.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.service.FridgeService;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.fixture.FridgeFixture;
import com.hyewon.grocey_api.fixture.UserFixture;
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
    @Mock private UserQueryService userQueryService;
    @InjectMocks private FridgeService fridgeService;

    private Fridge fridge;
    private User user;

    @BeforeEach
    void setUp() {
        fridge = FridgeFixture.aFridge();
        user = UserFixture.aDefaultUser();
        user.assignFridge(fridge);
    }

    @Test
    @DisplayName("returns fridge temperature info when user exists")
    void shouldReturnFridgeTemperatureInfo_whenUserExists() {
        // given
        Long userId = 1L;

        given(userQueryService.getUserById(userId)).willReturn(user);
        // when
        FridgeResponse response = fridgeService.getFridgeInfo(userId);

        // then
        assertThat(response.getFridgeTemperature()).isEqualTo(4.0);
        assertThat(response.getFreezerTemperature()).isEqualTo(-18.0);
    }
}