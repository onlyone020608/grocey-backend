package com.hyewon.grocey_api.integration.fridge;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FridgeController Integration Test")
public class FridgeControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/fridge - should return fridge info for authenticated user")
    void getFridge_shouldReturnFridgeInfo() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);

        // when & then
        mockMvc.perform(get("/api/fridge")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fridgeTemperature").exists())
                .andExpect(jsonPath("$.freezerTemperature").exists());
    }

}
