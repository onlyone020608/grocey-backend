package com.hyewon.grocey_api.domain.order;

import com.hyewon.grocey_api.domain.order.dto.OrderDetailDto;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
