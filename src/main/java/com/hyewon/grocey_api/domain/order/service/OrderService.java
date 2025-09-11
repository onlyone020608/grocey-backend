package com.hyewon.grocey_api.domain.order.service;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.service.CartItemService;
import com.hyewon.grocey_api.domain.order.dto.OrderDetailResponse;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryResponse;
import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.repository.OrderRepository;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.service.UserQueryService;
import com.hyewon.grocey_api.global.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserQueryService userQueryService;
    private final CartItemService cartItemService;

    @Transactional(readOnly = true)
    public  List<OrderSummaryResponse> getRecentOrderSummaryByUserId(Long userId) {
        List<Order> recentOrders = orderRepository.findRecentOrders(userId);

        return recentOrders.stream()
                .map(OrderSummaryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserIdWithItemsAndProduct(orderId, userId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        return OrderDetailResponse.from(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getAllOrders(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(OrderSummaryResponse::from);
    }

    @Transactional
    public Long placeOrder(Long userId, OrderRequest request) {
        User user = userQueryService.getUserById(userId);

        List<CartItem> selectedItems = cartItemService.getCartItemsWithProduct(request.cartItemIds(), userId);

        if (selectedItems.size() != request.cartItemIds().size()) {
            throw new AccessDeniedException("Some items do not belong to this user.");
        }

        Order order = Order.of(user, request.address(), request.toPaymentMethod());
        order.addOrderItems(selectedItems);

        orderRepository.save(order);
        cartItemService.deleteCartItems(selectedItems);
        return order.getId();
    }
}
