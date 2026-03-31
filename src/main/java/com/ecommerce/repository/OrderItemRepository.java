package com.ecommerce.repository;

import com.ecommerce.entity.OrderItem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // TODO: Add custom queries (e.g., findByOrderId)
    List<OrderItem> findByOrderId(Long OrderId);
}
