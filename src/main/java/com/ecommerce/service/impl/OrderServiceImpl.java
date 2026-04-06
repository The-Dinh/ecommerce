package com.ecommerce.service.impl;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.OrderItemDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.User;
import com.ecommerce.enumtype.OrderStatus;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
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
    public OrderDTO createMyOrder(String email, OrderDTO orderDTO) {
        User user = getUserByEmail(email);

        Order order = Order.builder()
                .orderDate(orderDTO.getOrderDate())
                .totalAmount(orderDTO.getTotalAmount())
                .shippingAddress(orderDTO.getShippingAddress())
                .paymentMethod(orderDTO.getPaymentMethod())
                .paymentStatus(orderDTO.getPaymentStatus())
                .orderStatus(orderDTO.getOrderStatus())
                .user(user)
                .build();

        Order savedOrder = orderRepository.save(order);
        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> getMyOrders(String email) {
        User user = getUserByEmail(email);
        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OrderDTO getMyOrderById(String email, Long orderId) {
        User user = getUserByEmail(email);
        Order order = getOrderEntityById(orderId);

        validateOrderOwnership(user, order);
        return mapToDTO(order);
    }

    @Override
    public OrderDTO cancelMyOrder(String email, Long orderId) {
        User user = getUserByEmail(email);
        Order order = getOrderEntityById(orderId);

        validateOrderOwnership(user, order);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Only pending orders can be cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        return mapToDTO(updatedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return mapToDTO(getOrderEntityById(orderId));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderEntityById(orderId);
        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapToDTO(updatedOrder);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private Order getOrderEntityById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    private void validateOrderOwnership(User user, Order order) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You do not have permission to access this order");
        }
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

    private OrderDTO mapToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .orderItems(order.getOrderItems() == null
                        ? List.of()
                        : order.getOrderItems().stream().map(this::mapToOrderItemDTO).toList())
                .build();
    }
}
