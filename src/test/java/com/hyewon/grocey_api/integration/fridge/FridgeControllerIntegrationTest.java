package com.hyewon.grocey_api.integration.fridge;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FridgeController Integration Test")
public class FridgeControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/fridge - returns fridge info when user is authenticated")
    void getFridge_withAuthenticatedUser_returnsFridgeInfo() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);

        // when & then
        mockMvc.perform(get("/api/fridge")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fridgeTemperature").exists())
                .andExpect(jsonPath("$.freezerTemperature").exists());
    }
}
