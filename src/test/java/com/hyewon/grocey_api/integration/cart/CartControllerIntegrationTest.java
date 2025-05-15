package com.hyewon.grocey_api.integration.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CartController Integration Test")
@Sql(scripts = {
        "/sql/ingredient-data.sql",
        "/sql/product-data.sql"
       })
public class CartControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("POST /api/cart/items - should add cart item")
    void addCartItem_shouldSucceed() throws Exception {
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();

        AddCartItemRequest request = new AddCartItemRequest(product.getId(), 2);

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/cart - should return cart with items")
    void getCart_shouldReturnCartInfo() throws Exception {
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddCartItemRequest(product.getId(), 2))))
                .andExpect(status().isCreated());


        mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(product.getId()))
                .andExpect(jsonPath("$.items[0].productName").value(product.getProductName()));
    }
}
