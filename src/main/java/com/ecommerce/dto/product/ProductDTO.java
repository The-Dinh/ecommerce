package com.ecommerce.dto.product;

import com.ecommerce.enumtype.ProductStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

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

    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name must be at most 255 characters")
    private String name;

    @Size(max = 100, message = "Brand must be at most 100 characters")
    private String brand;

    @Size(max = 2000, message = "Description must be at most 2000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity must be at least 0")
    private Integer stockQuantity;

    @Size(max = 1000, message = "Image URL must be at most 1000 characters")
    private String imageUrl;

    @NotNull(message = "Product status is required")
    private ProductStatus status;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @NotNull(message = "Category id is required")
    @Positive(message = "Category id must be greater than 0")
    private Long categoryId;

    private String categoryName;
}

