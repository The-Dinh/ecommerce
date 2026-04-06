package com.ecommerce.repository;

import com.ecommerce.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // TODO: Add custom queries (e.g., findByEmail)
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
