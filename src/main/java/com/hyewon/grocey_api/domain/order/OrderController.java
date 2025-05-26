package com.hyewon.grocey_api.domain.order;

import com.hyewon.grocey_api.domain.order.dto.OrderDetailDto;
import com.hyewon.grocey_api.domain.order.dto.OrderRequest;
import com.hyewon.grocey_api.domain.order.dto.OrderSummaryDto;
import com.hyewon.grocey_api.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("summary")
    public List<OrderSummaryDto> getSummary(@AuthenticationPrincipal CustomUserDetails userDetails){
        return orderService.getRecentOrderSummaryByUserId(userDetails.getId());
    }


    @GetMapping("{orderId}")
    public OrderDetailDto getOrderDetail(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long orderId){
        return orderService.getOrderDetail(userDetails.getId(), orderId);
    }

    @GetMapping
    public Page<OrderSummaryDto> getAllOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.getAllOrders(userDetails.getId(), pageable);
    }


    @PostMapping
    public ResponseEntity<Map<String, Long>> placeOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody OrderRequest request) {
        Long orderId = orderService.placeOrder(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("orderId", orderId));
    }


}
