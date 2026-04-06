package com.ecommerce.controller;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.enumtype.OrderStatus;
import com.ecommerce.exception.ApiResponse;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/me")
    public ResponseEntity<ApiResponse<OrderDTO>> createMyOrder(
            Authentication authentication,
            @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createMyOrder(authentication.getName(), orderDTO);
        return ResponseEntity.status(201).body(ApiResponse.created(createdOrder));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getMyOrders(Authentication authentication) {
        List<OrderDTO> orders = orderService.getMyOrders(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/me/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getMyOrderById(
            Authentication authentication,
            @PathVariable Long orderId) {
        OrderDTO order = orderService.getMyOrderById(authentication.getName(), orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PatchMapping("/me/{orderId}/cancel")
    public ResponseEntity<ApiResponse<OrderDTO>> cancelMyOrder(
            Authentication authentication,
            @PathVariable Long orderId) {
        OrderDTO cancelledOrder = orderService.cancelMyOrder(authentication.getName(), orderId);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled successfully", cancelledOrder));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", updatedOrder));
    }
}
