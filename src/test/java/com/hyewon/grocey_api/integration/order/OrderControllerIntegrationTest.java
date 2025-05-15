package com.hyewon.grocey_api.integration.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.cart.CartItem;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OrderController Integration Test")
@Sql(scripts = {
        "/sql/ingredient-data.sql",
        "/sql/product-data.sql"
})
public class OrderControllerIntegrationTest extends AbstractIntegrationTest {
    @Test
    @DisplayName("POST /api/orders - should create order")
    void placeOrder_shouldSucceed() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();
        Long cartItemId = addCartItemFor(user, product, 2).getId();

        OrderRequest request = new OrderRequest(
                List.of(cartItemId),
                "Gangnam-gu, Seoul",
                "KAKAOPAY"
        );

        // when & then
        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("GET /api/orders/summary - should return recent order summary")
    void getOrderSummary_shouldReturnSummary() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();
        CartItem item = addCartItemFor(user, product, 2);

        OrderRequest request = new OrderRequest(
                List.of(item.getId()),
                "Gangnam-gu, Seoul",
                "KAKAOPAY"
        );

        mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // when & then
        mockMvc.perform(get("/api/orders/summary")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").exists())
                .andExpect(jsonPath("$[0].orderStatus").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].items[0].productName").value(product.getProductName()));
    }

    @Test
    @DisplayName("GET /api/orders/{orderId} - should return order detail")
    void getOrderDetail_shouldReturnDetail() throws Exception {
        // given
        User user = createTestUser("Mary", "mary@example.com", "securepw");
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();
        CartItem item = addCartItemFor(user, product, 2);


        OrderRequest request = new OrderRequest(
                List.of(item.getId()),
                "Gangnam-gu, Seoul",
                "KAKAOPAY"
        );

        String responseBody = mockMvc.perform(post("/api/orders")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(responseBody);
        Long orderId = json.get("orderId").asLong();

        // when & then
        mockMvc.perform(get("/api/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.orderStatus").value("CONFIRMED"))
                .andExpect(jsonPath("$.paymentMethod").value("KAKAOPAY"))
                .andExpect(jsonPath("$.shippingAddress").value("Gangnam-gu, Seoul"))
                .andExpect(jsonPath("$.items[0].productName").value(product.getProductName()));
    }





}
