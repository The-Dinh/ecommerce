package com.ecommerce.service.impl;

import com.ecommerce.dto.order.CheckoutRequest;
import com.ecommerce.dto.order.OrderDTO;
import com.ecommerce.dto.order.OrderItemDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderItem;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.enumtype.OrderStatus;
import com.ecommerce.enumtype.PaymentStatus;
import com.ecommerce.enumtype.ProductStatus;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public OrderDTO createMyOrder(String email, CheckoutRequest request) {
        User user = getUserByEmail(email);
        Cart cart = getCartByUserId(user.getId());
        List<CartItem> cartItems = cart.getCartItems() == null ? List.of() : cart.getCartItems();

        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cannot checkout because cart is empty");
        }

        validateStockBeforeCheckout(cartItems);

        Order order = Order.builder()
                .orderDate(LocalDateTime.now())
                .shippingAddress(request.getShippingAddress())
                .paymentMethod(request.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .orderStatus(OrderStatus.PENDING)
                .user(user)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            BigDecimal unitPrice = product.getPrice();
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .build();
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(subtotal);

            int remainingStock = product.getStockQuantity() - cartItem.getQuantity();
            product.setStockQuantity(remainingStock);
            if (remainingStock == 0) {
                product.setStatus(ProductStatus.OUT_OF_STOCK);
            }
        }

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByCartId(cart.getId());
        cart.getCartItems().clear();

        return mapToDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> getMyOrders(String email) {
        User user = getUserByEmail(email);
        return orderRepository.findByUserId(user.getId())
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OrderDTO getMyOrderById(String email, Long orderId) {
        User user = getUserByEmail(email);
        Order order = getOrderEntityById(orderId);

        validateOrderOwnership(user, order);
        return mapToDTO(order);
    }

    @Override
    public OrderDTO cancelMyOrder(String email, Long orderId) {
        User user = getUserByEmail(email);
        Order order = getOrderEntityById(orderId);

        validateOrderOwnership(user, order);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Only pending orders can be cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        Order updatedOrder = orderRepository.save(order);
        return mapToDTO(updatedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return mapToDTO(getOrderEntityById(orderId));
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrderEntityById(orderId);
        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return mapToDTO(updatedOrder);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
    }

    // Check product active & đủ stock trước khi checkout
    private void validateStockBeforeCheckout(List<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            if (product.getStatus() != ProductStatus.ACTIVE) {
                throw new BadRequestException("Product is not available: " + product.getName());
            }
            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
        }
    }

    private Order getOrderEntityById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    private void validateOrderOwnership(User user, Order order) {
        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You do not have permission to access this order");
        }
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .subtotal(orderItem.getSubtotal())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .build();
    }

    private OrderDTO mapToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .orderStatus(order.getOrderStatus())
                .orderItems(order.getOrderItems() == null
                        ? List.of()
                        : order.getOrderItems().stream().map(this::mapToOrderItemDTO).toList())
                .userFullName(order.getUser() != null ? order.getUser().getFullName() : null)
                .userEmail(order.getUser() != null ? order.getUser().getEmail() : null)
                .build();
    }
}

