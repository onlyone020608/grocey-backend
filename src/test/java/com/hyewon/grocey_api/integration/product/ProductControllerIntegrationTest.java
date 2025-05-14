package com.hyewon.grocey_api.integration.product;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("ProductController Integration Test")
@Sql(scripts = {
        "/sql/ingredient-data.sql",
        "/sql/product-data.sql",
        "/sql/product-tab-data.sql"
})
public class ProductControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("GET /api/products?tab=NEW - should return products in NEW tab")
    void getProductsByTab_shouldReturnNewTabProducts() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();

        // when & then
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token)
                        .param("tab", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].brandName").value(product.getBrandName()))
                .andExpect(jsonPath("$[0].productName").value(product.getProductName()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].imageUrl").value(product.getImageUrl()));
    }

    @Test
    @DisplayName("GET /api/products?tab=BEST - should return products in BEST tab")
    void getProductsByTab_shouldReturnBestTabProducts() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(2L).orElseThrow();

        // when & then
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token)
                        .param("tab", "BEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].brandName").value(product.getBrandName()))
                .andExpect(jsonPath("$[0].productName").value(product.getProductName()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].imageUrl").value(product.getImageUrl()));
    }
}
