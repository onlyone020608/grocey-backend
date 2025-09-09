package com.hyewon.grocey_api.domain.order.service;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.service.CartItemService;
import com.hyewon.grocey_api.domain.order.dto.OrderDetailResponse;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryResponse;
import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.OrderItem;
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
                .map(OrderSummaryResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to view this order.");
        }

        return new OrderDetailResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryResponse> getAllOrders(Long userId, Pageable pageable) {
        User user = userQueryService.getUserById(userId);

        return orderRepository.findByUser(user, pageable)
                .map(OrderSummaryResponse::new);
    }

    @Transactional
    public Long placeOrder(Long userId, OrderRequest request) {
        User user = userQueryService.getUserById(userId);

        List<CartItem> selectedItems = cartItemService.getCartItems(request.getCartItemIds());

        for (CartItem item : selectedItems) {
            if (!item.getCart().getUser().getId().equals(userId)) {
                throw new AccessDeniedException("You cannot order items not in your cart.");
            }
        }

        Order order = Order.of(user, request.getAddress(), request.toPaymentMethod());

        for (CartItem item : selectedItems) {
            OrderItem orderItem = OrderItem.of(order, item.getProduct(), item.getQuantity(), item.getProduct().getPrice());
            order.addItem(orderItem);
        }

        orderRepository.save(order);
        cartItemService.deleteCartItems(selectedItems);
        return order.getId();
    }
}
