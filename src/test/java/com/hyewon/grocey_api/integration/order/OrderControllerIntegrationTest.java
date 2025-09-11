package com.hyewon.grocey_api.integration.order;

import com.hyewon.grocey_api.common.AbstractIntegrationTest;
import com.hyewon.grocey_api.domain.cart.entity.Cart;
import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.repository.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.repository.CartRepository;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.repository.OrderRepository;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.fixture.OrderFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OrderController Integration Test")
public class OrderControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired  private OrderRepository orderRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private CartItemRepository cartItemRepository;

    @Test
    @DisplayName("POST /api/orders - creates order when request is valid")
    void placeOrder_withValidRequest_createsOrder() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();
        Cart cart = cartRepository.save(Cart.of(user, user.getFridge()));
        CartItem cartItem = CartItem.of(product, 2);
        cart.addCartItem(cartItem);
        CartItem savedCartItem = cartItemRepository.save(cartItem);

        OrderRequest request = new OrderRequest(
                List.of(savedCartItem.getId()),
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
    @DisplayName("GET /api/orders/summary - returns recent order summary when orders exist")
    void getOrderSummary_withExistingOrders_returnsSummary() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();
        orderRepository.save(OrderFixture.createOrder(user, product, 2));

        // when & then
        mockMvc.perform(get("/api/orders/summary")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").exists())
                .andExpect(jsonPath("$[0].orderStatus").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].items[0].productName").value(product.getName()));
    }

    @Test
    @DisplayName("GET /api/orders/{orderId} - returns order detail when id is valid")
    void getOrderDetail_withValidId_returnsOrderDetail() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();
        Order order = orderRepository.save(OrderFixture.createOrder(user, product, 2));

        // when & then
        mockMvc.perform(get("/api/orders/" + order.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getId()))
                .andExpect(jsonPath("$.orderStatus").value("CONFIRMED"))
                .andExpect(jsonPath("$.paymentMethod").value("KAKAOPAY"))
                .andExpect(jsonPath("$.shippingAddress").value("Gangnam-gu, Seoul"))
                .andExpect(jsonPath("$.items[0].productName").value(product.getName()));
    }
    @Test
    @DisplayName("GET /api/orders - returns paged order list when orders exist")
    void getAllOrders_withExistingOrders_returnsPagedList() throws Exception {
        // given
        User user = createTestUser();
        String token = generateTokenFor(user);
        Product product = productRepository.findById(1L).orElseThrow();
        orderRepository.save(OrderFixture.createOrder(user, product, 2));

        // when & then
        mockMvc.perform(get("/api/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].orderId").exists())
                .andExpect(jsonPath("$.content[0].createdAt").exists())
                .andExpect(jsonPath("$.content[0].orderStatus").value("CONFIRMED"))
                .andExpect(jsonPath("$.content[0].items[0].productName").value(product.getName()));
    }
}
