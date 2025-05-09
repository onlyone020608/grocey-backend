package com.hyewon.grocey_api.domain.order;

import com.hyewon.grocey_api.domain.order.dto.OrderDetailDto;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class OrderController {

    private final OrderService orderService;

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT
    @GetMapping("{userId}/orders/summary")
    public List<OrderSummaryDto> getSummary(@PathVariable Long userId){
        return orderService.getRecentOrderSummaryByUserId(userId);
    }

    // TODO: Replace with @AuthenticationPrincipal after implementing JWT
    @GetMapping("{userId}/orders/{orderId}")
    public OrderDetailDto getOrderDetail(@PathVariable Long userId, @PathVariable Long orderId){
        return orderService.getOrderDetail(userId, orderId);
    }

    @GetMapping("/{userId}/orders")
    public Page<OrderSummaryDto> getAllOrders(
            @PathVariable Long userId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.getAllOrders(userId, pageable);
    }

}
