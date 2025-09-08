package com.hyewon.grocey_api.integration.cart;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.cart.dto.AddCartItemRequest;
import com.hyewon.grocey_api.domain.cart.dto.UpdateCartItemRequest;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("CartController Integration Test")
@Sql(scripts = {
        "/sql/ingredient-data.sql",
        "/sql/product-data.sql"
       })
public class CartControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("POST /api/cart/items - adds cart item when request is valid")
    void addCartItem_withValidRequest_addsItem() throws Exception {
        User user = createTestUser("Mary", "mary", "securepw");
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
    @DisplayName("GET /api/cart - returns cart with items when items exist")
    void getCart_withItems_returnsCartInfo() throws Exception {
        User user = createTestUser("Mary", "mary", "securepw");
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
                .andExpect(jsonPath("$.items[0].productName").value(product.getName()));
    }

    @Test
    @DisplayName("PATCH /api/cart/items - updates quantity when cart item exists")
    void updateCartItem_withValidId_updatesQuantity() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();

        AddCartItemRequest addRequest = new AddCartItemRequest(product.getId(), 2);
        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isCreated());

        String cartJson = mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode root = new ObjectMapper().readTree(cartJson);
        Long cartItemId = root.get("items").get(0).get("cartItemId").asLong();

        // when & then
        UpdateCartItemRequest updateRequest = new UpdateCartItemRequest(cartItemId, 5);

        mockMvc.perform(patch("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/cart/items - deletes selected items when multiple IDs are provided")
    void deleteCartItems_withMultipleIds_deletesSelectedItems() throws Exception {
        // given
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);
        Product product1 = productRepository.findById(1L).orElseThrow();
        Product product2 = productRepository.findById(2L).orElseThrow();

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddCartItemRequest(product1.getId(), 2))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new AddCartItemRequest(product2.getId(), 1))))
                .andExpect(status().isCreated());

        String cartJson = mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode root = objectMapper.readTree(cartJson);
        JsonNode items = root.get("items");
        Long cartItemId1 = items.get(0).get("cartItemId").asLong();
        Long cartItemId2 = items.get(1).get("cartItemId").asLong();

        // when & then
        mockMvc.perform(delete("/api/cart/items")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[" + cartItemId1 + "," + cartItemId2 + "]"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    @DisplayName("POST /api/cart/items/batch - adds multiple items when request is valid")
    void addCartItemsInBatch_withValidRequest_addsMultipleItems() throws Exception {
        User user = createTestUser("Mary", "mary", "securepw");
        String token = generateTokenFor(user);
        Product product1 = productRepository.findById(1L).orElseThrow();
        Product product2 = productRepository.findById(2L).orElseThrow();

        List<AddCartItemRequest> requests = List.of(
                new AddCartItemRequest(product1.getId(), 2),
                new AddCartItemRequest(product2.getId(), 1)
        );

        mockMvc.perform(post("/api/cart/items/batch")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(product1.getId()))
                .andExpect(jsonPath("$.items[1].productId").value(product2.getId()));
    }
}
