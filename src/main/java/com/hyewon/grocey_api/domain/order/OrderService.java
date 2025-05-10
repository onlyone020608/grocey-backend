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

    public  List<OrderSummaryDto> getRecentOrderSummaryByUserId(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        List<Order> recentOrders = orderRepository.findTop5ByUserOrderByCreatedAtDesc(user);

        return recentOrders.stream()
                .map(OrderSummaryDto::new)
                .toList();

    }

    public OrderDetailDto getOrderDetail(Long userId, Long orderId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        if (!order.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 주문은 유저의 주문이 아닙니다.");
        }

        return new OrderDetailDto(order);

    }

    public Page<OrderSummaryDto> getAllOrders(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return orderRepository.findByUser(user, pageable)
                .map(OrderSummaryDto::new);
    }

    public void placeOrder(Long userId, OrderRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException(userId));

        List<CartItem> selectedItems = cartItemRepository.findAllById(request.getCartItemIds());
        if (selectedItems.isEmpty()) {
            throw new InvalidRequestException("선택된 장바구니 항목이 없습니다.");
        }

        Order order = new Order(user, request.getAddress(), request.toPaymentMethod());

        for (CartItem item : selectedItems) {
            OrderItem orderItem = new OrderItem(order, item.getProduct(), item.getQuantity(), item.getProduct().getPrice());
            order.getOrderItems().add(orderItem);
        }

        orderRepository.save(order);
        cartItemRepository.deleteAll(selectedItems); // 선택된 항목만 장바구니에서 제거
    }








}
