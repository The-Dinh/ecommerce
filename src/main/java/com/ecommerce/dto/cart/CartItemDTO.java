package com.ecommerce.dto.cart;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO {
    // TODO: Add fields (id, productId, productName, quantity, unitPrice,
    // totalPrice)
    private Long id;

    private Integer quantity;

    private BigDecimal unitPrice;

    private Long productId;

    private String productName;
}

