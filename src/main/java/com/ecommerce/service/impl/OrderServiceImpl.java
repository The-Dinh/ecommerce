package com.ecommerce.service.impl;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderItemDTO;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
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
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        return maptoDTO(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        
        if(orders.isEmpty()) {
            throw new RuntimeException("No orders found for user id: " + userId);    
        }
        return orders.stream()
                .map(this::maptoDTO)
                .toList(); 
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if(orders.isEmpty()) {
            throw new RuntimeException("No orders found");    
        }
        return orders.stream()
                .map(this::maptoDTO)
                .toList(); 
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return maptoDTO(updatedOrder); 
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subtotal(orderItem.getSubtotal())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .build();
    }

    private OrderDTO maptoDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .orderItems(order.getOrderItems()
                        .stream()
                        .map(this::mapToOrderItemDTO)
                        .toList())
                .build();
    }
}
