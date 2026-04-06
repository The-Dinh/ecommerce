package com.ecommerce.dto;

import com.ecommerce.enumtype.OrderStatus;
import com.ecommerce.enumtype.PaymentStatus;

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
    // TODO: Add fields (id, userId, orderDate, totalAmount, shippingAddress,
    // paymentMethod, paymentStatus, orderStatus, orderItems)
    private Long id;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount;

    private String shippingAddress;

    private String paymentMethod;

    private PaymentStatus paymentStatus;

    private OrderStatus orderStatus;

    private List<OrderItemDTO> orderItems;
}
