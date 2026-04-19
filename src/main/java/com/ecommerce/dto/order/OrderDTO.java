package com.ecommerce.dto.order;

import com.ecommerce.enumtype.OrderStatus;
import com.ecommerce.enumtype.PaymentStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;

    private LocalDateTime orderDate;

    @NotNull(message = "Total amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @NotBlank(message = "Shipping address is required")
    @Size(max = 500, message = "Shipping address must be at most 500 characters")
    private String shippingAddress;

    @NotBlank(message = "Payment method is required")
    @Size(max = 100, message = "Payment method must be at most 100 characters")
    private String paymentMethod;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus;

    @NotNull(message = "Order status is required")
    private OrderStatus orderStatus;

    @Valid
    private List<OrderItemDTO> orderItems;

    private String userFullName;
    private String userEmail;
}

