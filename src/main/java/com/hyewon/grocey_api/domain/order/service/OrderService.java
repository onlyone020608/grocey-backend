package com.hyewon.grocey_api.domain.order.service;

import com.hyewon.grocey_api.domain.cart.entity.CartItem;
import com.hyewon.grocey_api.domain.cart.repository.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.repository.CartRepository;
import com.hyewon.grocey_api.domain.order.repository.OrderRepository;
import com.hyewon.grocey_api.domain.order.dto.OrderDetailDto;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryDto;
import com.hyewon.grocey_api.domain.order.entity.Order;
import com.hyewon.grocey_api.domain.order.entity.OrderItem;
import com.hyewon.grocey_api.domain.user.entity.User;
import com.hyewon.grocey_api.domain.user.repository.UserRepository;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import com.hyewon.grocey_api.global.exception.OrderNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
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
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional(readOnly = true)
    public  List<OrderSummaryDto> getRecentOrderSummaryByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Order> recentOrders = orderRepository.findTop5ByUserOrderByCreatedAtDesc(user);

        return recentOrders.stream()
                .map(OrderSummaryDto::new)
                .toList();

    }
    @Transactional(readOnly = true)
    public OrderDetailDto getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to view this order.");
        }

        return new OrderDetailDto(order);

    }

    @Transactional(readOnly = true)
    public Page<OrderSummaryDto> getAllOrders(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return orderRepository.findByUser(user, pageable)
                .map(OrderSummaryDto::new);
    }

    @Transactional
    public Long placeOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));


        List<CartItem> selectedItems = cartItemRepository.findAllById(request.getCartItemIds());
        if (selectedItems.isEmpty()) {
            throw new InvalidRequestException("No cart items selected.");
        }

        for (CartItem item : selectedItems) {
            if (!item.getCart().getUser().getId().equals(userId)) {
                throw new AccessDeniedException("You cannot order items not in your cart.");
            }
        }

        Order order = new Order(user, request.getAddress(), request.toPaymentMethod());

        for (CartItem item : selectedItems) {
            OrderItem orderItem = new OrderItem(order, item.getProduct(), item.getQuantity(), item.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);
        cartItemRepository.deleteAll(selectedItems);
        return order.getId();
    }








}
