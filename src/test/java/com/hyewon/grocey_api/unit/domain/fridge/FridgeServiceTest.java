package com.hyewon.grocey_api.unit.domain.fridge;

import com.hyewon.grocey_api.domain.fridge.dto.FridgeResponse;
import com.hyewon.grocey_api.domain.fridge.entity.Fridge;
import com.hyewon.grocey_api.domain.fridge.repository.FridgeRepository;
import com.hyewon.grocey_api.domain.fridge.service.FridgeService;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.global.exception.FridgeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FridgeServiceTest {
    @Mock private FridgeRepository fridgeRepository;
    @Mock private UserQueryService userQueryService;
    @InjectMocks private FridgeService fridgeService;

    private Fridge fridge;
    private User user;

    @BeforeEach
    void setUp() {
        fridge = Fridge.builder()
                .fridgeTemperature(4.0)
                .freezerTemperature(-18.0)
                .build();
        user = User.builder()
                .id(1L)
                .fridge(fridge)
                .build();
    }

    @Test
    @DisplayName("getFridgeInfo - returns fridge temperature info successfully")
    void getFridgeInfo_shouldReturnTemperature() {
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