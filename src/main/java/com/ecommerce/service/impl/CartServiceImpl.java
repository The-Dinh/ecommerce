package com.ecommerce.service.impl;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.CartItemDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.Product;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    // TODO: Inject CartRepository, CartItemRepository, ProductRepository
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public CartDTO getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));
        return mapToDTO(cart);
    }

    @Override
    public CartDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) {

        if (cartItemDTO.getQuantity() == null || cartItemDTO.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartItemDTO.getProductId()));

        CartItem existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(cartItemDTO.getProductId()))
                .findFirst()
                .orElse(null);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + cartItemDTO.getQuantity());
            existingCartItem.setUnitPrice(product.getPrice());
            cartItemRepository.save(existingCartItem);
        } else {
            CartItem newCartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(cartItemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            cartItemRepository.save(newCartItem);
        }

        Cart updatedCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));

        return mapToDTO(updatedCart);
    }

    @Override
    public CartDTO updateCartItem(Long userId, Long cartItemId, CartItemDTO cartItemDTO) {
        if(cartItemDTO.getQuantity() == null || cartItemDTO.getQuantity() <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with userId: " + userId));
        
        if(!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("CartItem does not belong to this user's cart");
        }

        cartItem.setQuantity(cartItemDTO.getQuantity());

        cartItemRepository.save(cartItem);

        Cart updatedCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));
        return mapToDTO(updatedCart); 
    }

    @Override
    public CartDTO removeCartItem(Long userId, Long cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found with userId: " + userId));
        if(!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("CartItem does not belong to this user's cart");
        }

        cartItemRepository.delete(cartItem);
        
        Cart updateCart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));
        return mapToDTO(updateCart);
    }

    @Override
    // lỗi giữa chừng rollback
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found with userId: " + userId));
        cartItemRepository.deleteByCartId(cart.getId());
        cart.getCartItems().clear();
    }

    private CartItemDTO mapToCartItemDTO(CartItem cartItem) {
        return CartItemDTO.builder()
                .id(cartItem.getId())
                .quantity(cartItem.getQuantity())
                .unitPrice(cartItem.getUnitPrice())
                .productId(cartItem.getProduct().getId())
                .productName(cartItem.getProduct().getName())
                .build();
    }

    private CartDTO mapToDTO(Cart cart) {
        return CartDTO.builder()
                .id(cart.getId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .userId(cart.getUser().getId())
                .cartItems(cart.getCartItems()
                        .stream()
                        .map(this::mapToCartItemDTO)
                        .toList())
                .build();
    }
}
