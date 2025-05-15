package com.hyewon.grocey_api.integration.recommendation;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.recommendation.FridgeRecommendation;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("FridgeRecommendationController Integration Test")
@Sql(scripts = {
        "/sql/ingredient-data.sql",
        "/sql/product-data.sql",
})
public class FridgeRecommendationControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/recommendations/fridge - should return fridge recommendation result")
    void getFridgeRecommendation_shouldReturnRecommendation() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);

        Product product = productRepository.findById(1L).orElseThrow();
        FridgeRecommendation recommendation = setupFridgeRecommendation(user);
        setupRecommendedProduct(product, recommendation);

        // when & then
        mockMvc.perform(get("/api/recommendations/fridge")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recommendationId").value(recommendation.getId()))
                .andExpect(jsonPath("$.products[0].productId").value(product.getId()))
                .andExpect(jsonPath("$.products[0].productName").value(product.getProductName()))
                .andExpect(jsonPath("$.products[0].brand").value(product.getBrandName()))
                .andExpect(jsonPath("$.products[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$.products[0].imageUrl").value(product.getImageUrl()));
    }

}
