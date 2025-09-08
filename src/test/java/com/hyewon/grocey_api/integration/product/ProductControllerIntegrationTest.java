package com.hyewon.grocey_api.integration.product;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

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
    @DisplayName("GET /api/products?tab=NEW - returns products when tab is NEW")
    void getProductsByTab_withNewTab_returnsNewProducts() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();

        // when & then
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token)
                        .param("tab", "NEW"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].brandName").value(product.getBrand()))
                .andExpect(jsonPath("$[0].productName").value(product.getName()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].imageUrl").value(product.getImageUrl()));
    }

    @Test
    @DisplayName("GET /api/products?tab=BEST - returns products when tab is BEST")
    void getProductsByTab_withBestTab_returnsBestProducts() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(2L).orElseThrow();

        // when & then
        mockMvc.perform(get("/api/products")
                        .header("Authorization", "Bearer " + token)
                        .param("tab", "BEST"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].brandName").value(product.getBrand()))
                .andExpect(jsonPath("$[0].productName").value(product.getName()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].imageUrl").value(product.getImageUrl()));
    }

    @Test
    @DisplayName("GET /api/products/{productId} - returns product detail when id is valid")
    void getProductById_withValidId_returnsProductDetail() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();

        // when & then
        mockMvc.perform(get("/api/products/{productId}", product.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(product.getId()))
                .andExpect(jsonPath("$.brandName").value(product.getBrand()))
                .andExpect(jsonPath("$.productName").value(product.getName()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.imageUrl").value(product.getImageUrl()));
    }
}
