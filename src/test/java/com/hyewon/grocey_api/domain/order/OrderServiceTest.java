package com.hyewon.grocey_api.domain.order;

import com.hyewon.grocey_api.domain.cart.Cart;
import com.hyewon.grocey_api.domain.cart.CartItem;
import com.hyewon.grocey_api.domain.cart.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.CartRepository;
import com.hyewon.grocey_api.domain.order.dto.OrderDetailDto;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryDto;
import com.hyewon.grocey_api.domain.product.Product;
import com.hyewon.grocey_api.domain.user.AgeGroup;
import com.hyewon.grocey_api.domain.user.Gender;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        user = new User("tester", "test@email.com", "pw", AgeGroup.TWENTIES, Gender.FEMALE);
        ReflectionTestUtils.setField(user, "id", 1L);

        cart = new Cart(user, null);
        product = new Product("Milk", "SeoulDairy", 2000, "milk.png");
        cartItem = new CartItem(product, 2);
        cart.addCartItem(cartItem);
        ReflectionTestUtils.setField(cartItem, "id", 10L);
    }

    @Test
    @DisplayName("placeOrder - successfully places order with valid cart items")
    void placeOrder_shouldSucceedWithValidItems() {
        // given
        OrderRequest request = mock(OrderRequest.class);
        given(request.getCartItemIds()).willReturn(List.of(10L));
        given(request.getAddress()).willReturn("123 Seoul Street");
        given(request.toPaymentMethod()).willReturn(PaymentMethod.KAKAOPAY);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(cartItemRepository.findAllById(List.of(10L))).willReturn(List.of(cartItem));

        given(orderRepository.save(any(Order.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when & then
        assertThatNoException().isThrownBy(() -> orderService.placeOrder(1L, request));

        verify(orderRepository).save(any(Order.class));
        verify(cartItemRepository).deleteAll(List.of(cartItem));
    }

    @Test
    @DisplayName("placeOrder - throws InvalidRequestException when no cart items selected")
    void placeOrder_shouldThrowIfCartItemsEmpty() {
        // given
        OrderRequest request = mock(OrderRequest.class);
        given(request.getCartItemIds()).willReturn(List.of());

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(cartItemRepository.findAllById(any())).willReturn(List.of());

        // when & then
        assertThatThrownBy(() -> orderService.placeOrder(1L, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("No cart items selected");
    }

    @Test
    @DisplayName("placeOrder - throws AccessDeniedException when cart item does not belong to user")
    void placeOrder_shouldThrowIfCartItemNotOwnedByUser() {
        // given
        OrderRequest request = mock(OrderRequest.class);
        given(request.getCartItemIds()).willReturn(List.of(10L));

        // cartItem이 다른 유저의 것
        User anotherUser = new User("other", "other@email.com", "pw", AgeGroup.TWENTIES, Gender.MALE);
        ReflectionTestUtils.setField(anotherUser, "id", 999L);

        Cart anotherCart = new Cart(anotherUser, null);
        cartItem = new CartItem(product, 2);
        anotherCart.addCartItem(cartItem);
        ReflectionTestUtils.setField(cartItem, "id", 10L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(cartItemRepository.findAllById(List.of(10L))).willReturn(List.of(cartItem));

        // when & then
        assertThatThrownBy(() -> orderService.placeOrder(1L, request))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("You cannot order items not in your cart");
    }

    @Test
    @DisplayName("placeOrder - throws InvalidRequestException for invalid payment method")
    void placeOrder_shouldThrowForInvalidPaymentMethod() {
        // given
        OrderRequest request = new OrderRequest();
        ReflectionTestUtils.setField(request, "cartItemIds", List.of(10L));
        ReflectionTestUtils.setField(request, "address", "Somewhere");
        ReflectionTestUtils.setField(request, "paymentMethod", "bitcoin"); // ⚠️ 잘못된 값 intentionally

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(cartItemRepository.findAllById(any())).willReturn(List.of(cartItem));

        // when & then
        assertThatThrownBy(() -> orderService.placeOrder(1L, request))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("Invalid payment method");
    }

    @Test
    @DisplayName("getRecentOrderSummaryByUserId - returns list of recent orders")
    void getRecentOrderSummaryByUserId_shouldReturnSummaries() {
        // given
        ReflectionTestUtils.setField(user, "id", 1L);

        Order order = new Order(user, "Seoul", PaymentMethod.KAKAOPAY);
        ReflectionTestUtils.setField(order, "id", 100L);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(orderRepository.findTop5ByUserOrderByCreatedAtDesc(user)).willReturn(List.of(order));

        // when
        List<OrderSummaryDto> result = orderService.getRecentOrderSummaryByUserId(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getOrderId()).isEqualTo(100L);
        assertThat(result.get(0).getOrderStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    @DisplayName("getOrderDetail - returns order detail when user owns the order")
    void getOrderDetail_shouldReturnDetailIfUserOwnsOrder() {
        // given
        Long userId = 1L;
        ReflectionTestUtils.setField(user, "id", userId);

        Order order = new Order(user, "123 Seoul", PaymentMethod.KAKAOPAY);
        ReflectionTestUtils.setField(order, "id", 101L);

        given(orderRepository.findById(101L)).willReturn(Optional.of(order));

        // when
        OrderDetailDto result = orderService.getOrderDetail(userId, 101L);

        // then
        assertThat(result.getOrderId()).isEqualTo(101L);
        assertThat(result.getShippingAddress()).isEqualTo("123 Seoul");
        assertThat(result.getPaymentMethod()).isEqualTo(PaymentMethod.KAKAOPAY);
    }

    @Test
    @DisplayName("getOrderDetail - throws AccessDeniedException when user does not own the order")
    void getOrderDetail_shouldThrowIfUserDoesNotOwnOrder() {
        // given
        User anotherUser = new User("hacker", "bad@evil.com", "pw", AgeGroup.TWENTIES, Gender.MALE);
        ReflectionTestUtils.setField(anotherUser, "id", 999L);

        Order order = new Order(anotherUser, "hidden address", PaymentMethod.TOSS);
        ReflectionTestUtils.setField(order, "id", 101L);

        given(orderRepository.findById(101L)).willReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.getOrderDetail(1L, 101L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("not authorized");
    }

    @Test
    @DisplayName("getAllOrders - returns paginated list of order summaries")
    void getAllOrders_shouldReturnPagedSummaries() {
        // given
        ReflectionTestUtils.setField(user, "id", 1L);

        Pageable pageable = Pageable.ofSize(10);
        Order order = new Order(user, "123 Seoul", PaymentMethod.TOSS);
        ReflectionTestUtils.setField(order, "id", 100L);

        Page<Order> mockPage = new PageImpl<>(List.of(order), pageable, 1);

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(orderRepository.findByUser(user, pageable)).willReturn(mockPage);

        // when
        Page<OrderSummaryDto> result = orderService.getAllOrders(1L, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getOrderId()).isEqualTo(100L);
        assertThat(result.getContent().get(0).getOrderStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }









}