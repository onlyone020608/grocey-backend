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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("me/orders/summary")
    public List<OrderSummaryDto> getSummary(@AuthenticationPrincipal CustomUserDetails userDetails){
        return orderService.getRecentOrderSummaryByUserId(userDetails.getId());
    }


    @GetMapping("me/orders/{orderId}")
    public OrderDetailDto getOrderDetail(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long orderId){
        return orderService.getOrderDetail(userDetails.getId(), orderId);
    }

    @GetMapping("/me/orders")
    public Page<OrderSummaryDto> getAllOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return orderService.getAllOrders(userDetails.getId(), pageable);
    }


    @PostMapping("/me/orders")
    public ResponseEntity<Void> placeOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody OrderRequest request) {
        orderService.placeOrder(userDetails.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
