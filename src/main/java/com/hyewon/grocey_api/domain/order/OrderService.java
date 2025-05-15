package com.hyewon.grocey_api.domain.order;

import com.hyewon.grocey_api.domain.cart.Cart;
import com.hyewon.grocey_api.domain.cart.CartItem;
import com.hyewon.grocey_api.domain.cart.CartItemRepository;
import com.hyewon.grocey_api.domain.cart.CartRepository;
import com.hyewon.grocey_api.domain.order.dto.OrderDetailDto;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryDto;
import com.hyewon.grocey_api.domain.user.User;
import com.hyewon.grocey_api.domain.user.UserRepository;
import com.hyewon.grocey_api.global.exception.CartNotFoundException;
import com.hyewon.grocey_api.global.exception.InvalidRequestException;
import com.hyewon.grocey_api.global.exception.OrderNotFoundException;
import com.hyewon.grocey_api.global.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public  List<OrderSummaryDto> getRecentOrderSummaryByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Order> recentOrders = orderRepository.findTop5ByUserOrderByCreatedAtDesc(user);

        return recentOrders.stream()
                .map(OrderSummaryDto::new)
                .toList();

    }

    public OrderDetailDto getOrderDetail(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("You are not authorized to view this order.");
        }

        return new OrderDetailDto(order);

    }

    public Page<OrderSummaryDto> getAllOrders(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return orderRepository.findByUser(user, pageable)
                .map(OrderSummaryDto::new);
    }

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
