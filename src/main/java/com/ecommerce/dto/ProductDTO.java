package com.ecommerce.dto;

import com.ecommerce.enumtype.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    // TODO: Add fields (id, name, brand, description, price, stockQuantity,
    // imageUrl, status, categoryId, categoryName, createdAt)
    private Long id;

    private String name;

    private String brand;

    private String description;

    private BigDecimal price;

    private Integer stockQuantity;

    private String imageUrl;

    private ProductStatus status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Long categoryId;

    private String categoryName;
}
