package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.enumtype.OrderStatus;
import java.util.List;

public interface OrderService {

    OrderDTO createOrder(Long userId, OrderDTO orderDTO);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getOrdersByUserId(Long userId);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
}
