package com.hyewon.grocey_api.unit.domain.order;

import com.hyewon.grocey_api.domain.cart.entity.Cart;
import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.service.CartItemService;
import com.hyewon.grocey_api.domain.order.dto.OrderDetailResponse;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryResponse;
import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.PaymentMethod;
import com.hyewon.grocey_api.domain.order.repository.OrderRepository;
import com.hyewon.grocey_api.domain.order.service.OrderService;
import com.hyewon.grocey_api.domain.product.entity.Product;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.fixture.ProductFixture;
import com.hyewon.grocey_api.fixture.UserFixture;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import com.hyewon.grocey_api.global.exception.OrderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock private OrderRepository orderRepository;
    @Mock private UserQueryService userQueryService;
    @Mock private CartItemService cartItemService;
    @InjectMocks private OrderService orderService;

    private User user;
    private Cart cart;
    private Product product;
    private CartItem cartItem;
    private Order order;

    @BeforeEach
    void setUp() {
        user = UserFixture.aDefaultUser();
        cart = Cart.builder()
                .user(user)
                .fridge(null)
                .build();
        product = ProductFixture.aProduct().build();
        cartItem = CartItem.builder()
                .id(10L)
                .product(product)
                .quantity(2)
                .build();
        cart.addCartItem(cartItem);
        order = Order.builder()
                .id(101L)
                .user(user)
                .address("123 Seoul")
                .paymentMethod(PaymentMethod.KAKAOPAY)
                .build();
    }

    @Test
    @DisplayName("returns list of recent orders when user exists")
    void shouldReturnRecentOrders_whenUserExists() {
        // given
        Long userId = 1L;

        given(orderRepository.findRecentOrders(userId)).willReturn(List.of(order));

        // when
        List<OrderSummaryResponse> result = orderService.getRecentOrderSummaryByUserId(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(101L);
    }

    @Test
    @DisplayName("returns order detail when user owns the order")
    void shouldReturnOrderDetail_whenUserOwnsOrder() {
        // given
        Long userId = 1L;
        given(orderRepository.findByIdAndUserIdWithItemsAndProduct(101L, userId)).willReturn(Optional.of(order));

        // when
        OrderDetailResponse result = orderService.getOrderDetail(userId, 101L);

        // then
        assertThat(result.getOrderId()).isEqualTo(101L);
        assertThat(result.getShippingAddress()).isEqualTo("123 Seoul");
        assertThat(result.getPaymentMethod()).isEqualTo(PaymentMethod.KAKAOPAY);
    }

    @Test
    @DisplayName("throws AccessDeniedException when user does not own the order")
    void shouldThrowException_whenUserDoesNotOwnOrder() {
        // given
        order = Order.builder()
                .id(222L)
                .address("hidden address")
                .paymentMethod(PaymentMethod.TOSS)
                .build();

        given(orderRepository.findByIdAndUserIdWithItemsAndProduct(222L, 1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.getOrderDetail(1L, 222L))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    @DisplayName("returns paginated list of order summaries when user exists")
    void shouldReturnPagedOrderSummaries_whenUserExists() {
        // given
        Long userId = 1L;
        Pageable pageable = Pageable.ofSize(10);
        Page<Order> mockPage = new PageImpl<>(List.of(order), pageable, 1);

        given(orderRepository.findByUserId(userId, pageable)).willReturn(mockPage);

        // when
        Page<OrderSummaryResponse> result = orderService.getAllOrders(1L, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getOrderId()).isEqualTo(101L);
    }

    @Test
    @DisplayName("places order successfully when cart items are valid")
    void shouldPlaceOrder_whenCartItemsValid() {
        // given
        Long userId = 1L;
        OrderRequest request = new OrderRequest(List.of(10L), "123 Soul Street", "KAKAOPAY");

        given(userQueryService.getUserById(1L)).willReturn(user);
        given(cartItemService.getCartItemsWithProduct(List.of(10L), userId)).willReturn(List.of(cartItem));
        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when & then
        assertThatNoException().isThrownBy(() -> orderService.placeOrder(1L, request));
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("throws AccessDeniedException when cart item does not belong to user")
    void shouldThrowException_whenCartItemNotOwnedByUser() {
        // given
        OrderRequest request = new OrderRequest(List.of(10L), "123 Soul Street", "KAKAOPAY");

        // cartItem이 다른 유저의 것
        User anotherUser = User.builder()
                .id(999L)
                .username("hacker")
                .email("bad@evil.com")
                .password("pw")
                .build();

        Cart anotherCart = Cart.builder()
                .user(anotherUser)
                .fridge(null)
                .build();

        anotherCart.addCartItem(cartItem);

        given(userQueryService.getUserById(1L)).willReturn(user);
        given(cartItemService.getCartItemsWithProduct(List.of(10L), 1L)).willReturn(List.of());

        // when & then
        assertThatThrownBy(() -> orderService.placeOrder(1L, request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("Some items do not belong to this user.");
    }

    @Test
    @DisplayName("throws InvalidRequestException when payment method is invalid")
    void shouldThrowException_whenPaymentMethodInvalid() {
        // given
        Long userId = 1L;
        OrderRequest request = new OrderRequest(List.of(10L), "123 Soul Street", "bitcoin");

        given(userQueryService.getUserById(userId)).willReturn(user);
        given(cartItemService.getCartItemsWithProduct(List.of(10L), 1L)).willReturn(List.of(cartItem));

        // when & then
        assertThatThrownBy(() -> orderService.placeOrder(1L, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid payment method");
    }
}