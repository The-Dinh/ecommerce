package com.ecommerce.repository;

import com.ecommerce.entity.Cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // TODO: Add custom queries (e.g., findByUserId)
    Optional<Cart> findByUserId(Long userId);
}
