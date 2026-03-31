package com.ecommerce.controller;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.enumtype.OrderStatus;
import com.ecommerce.exception.ApiResponse;
import com.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    // TODO: Inject OrderService
    private final OrderService orderService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<OrderDTO>> createOrder(@PathVariable Long userId,
            @RequestBody OrderDTO orderDTO) {
        // TODO: Call service
        OrderDTO createdOrder = orderService.createOrder(userId, orderDTO);
        return ResponseEntity.status(201).body(ApiResponse.created(createdOrder));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrderById(@PathVariable Long orderId) {
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getOrdersByUserId(@PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(ApiResponse.success(orders));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(ApiResponse.success("Order status updated", updatedOrder));
    }
}
