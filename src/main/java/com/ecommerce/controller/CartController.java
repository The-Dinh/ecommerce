package com.ecommerce.controller;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.CartItemDTO;
import com.ecommerce.exception.ApiResponse;
import com.ecommerce.service.CartService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<CartDTO>> getCartByUserId(@PathVariable Long userId) {
        CartDTO cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<ApiResponse<CartDTO>> addItemToCart(@PathVariable Long userId,
            @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cart = cartService.addItemToCart(userId, cartItemDTO);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PutMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDTO>> updateCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId,
            @RequestBody CartItemDTO cartItemDTO) {
        // TODO: Call service
        CartDTO cart = new CartDTO();
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @DeleteMapping("/{userId}/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDTO>> removeCartItem(
            @PathVariable Long userId,
            @PathVariable Long cartItemId) {
        // TODO: Call service
        CartDTO cart = new CartDTO();
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cart));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<ApiResponse<Void>> clearCart(@PathVariable Long userId) {
        // TODO: Call service
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", null));
    }
}
