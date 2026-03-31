package com.ecommerce.dto;

import java.util.List;

import com.ecommerce.entity.CartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {
    // TODO: Add fields (id, userId, cartItems, totalAmount, createdAt, updatedAt)
    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long userId;

    private List<CartItemDTO> cartItems;
}
