package com.hyewon.grocey_api.integration.recommendation;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FridgeRecommendationController Integration Test")
public class FridgeRecommendationControllerIntegrationTest extends AbstractIntegrationTest {
    @MockitoBean
    private RestTemplate restTemplate;

    @Test
    @DisplayName("GET /api/recommendations/fridge - returns fridge recommendations when user has data")
    void getFridgeRecommendation_withUserData_returnsRecommendation() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);

        Product product = productRepository.findById(1L).orElseThrow();

        List<Long> aiReturnedIds = List.of(product.getId());
        String url = "http://grocey-ai:5001/api/recommend/" + user.getId();
        ResponseEntity<List> mockResponse = new ResponseEntity<>(aiReturnedIds, HttpStatus.OK);
        given(restTemplate.getForEntity(url, List.class)).willReturn(mockResponse);

        // when & then
        mockMvc.perform(get("/api/recommendations/fridge")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationId").exists())
                .andExpect(jsonPath("$.products[0].productId").exists());
    }
}
