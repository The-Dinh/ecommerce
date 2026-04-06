package com.ecommerce.service;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.enumtype.OrderStatus;
import java.util.List;

public interface OrderService {

    OrderDTO createMyOrder(String email, OrderDTO orderDTO);

    List<OrderDTO> getMyOrders(String email);

    OrderDTO getMyOrderById(String email, Long orderId);

    OrderDTO cancelMyOrder(String email, Long orderId);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrderStatus(Long orderId, OrderStatus status);
}
