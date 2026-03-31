package com.ecommerce.service.impl;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.enumtype.OrderStatus;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    // TODO: Inject OrderRepository, CartRepository, CartItemRepository

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    public OrderDTO createOrder(Long userId, OrderDTO orderDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        Order order = Order.builder()
                .orderDate(orderDTO.getOrderDate())
                .totalAmount(orderDTO.getTotalAmount())
                .shippingAddress(orderDTO.getShippingAddress())
                .paymentStatus(orderDTO.getPaymentStatus())
                .orderStatus(orderDTO.getOrderStatus())
                .user(user)
                .build();
        Order saveOrder = orderRepository.save(order);
        return maptoDTO(saveOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return null; // TODO: Implement
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return null; // TODO: Implement
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return null; // TODO: Implement
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        return null; // TODO: Implement
    }

    public OrderDTO maptoDTO(Order order) {
        return OrderDTO.builder()
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .build();
    }
}
