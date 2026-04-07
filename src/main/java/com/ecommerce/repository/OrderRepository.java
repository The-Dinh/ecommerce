package com.ecommerce.repository;

import com.ecommerce.entity.Order;
import com.ecommerce.enumtype.OrderStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // TODO: Add custom queries (e.g., findByUserId, findByOrderStatus)
    List<Order> findByUserId(Long UserId);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}

