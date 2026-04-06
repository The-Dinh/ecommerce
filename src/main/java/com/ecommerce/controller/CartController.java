package com.ecommerce.controller;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.CartItemDTO;
import com.ecommerce.exception.ApiResponse;
import com.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<CartDTO>> getMyCart(Authentication authentication) {
        CartDTO cart = cartService.getMyCart(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PostMapping("/me/items")
    public ResponseEntity<ApiResponse<CartDTO>> addItemToMyCart(
            Authentication authentication,
            @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cart = cartService.addItemToMyCart(authentication.getName(), cartItemDTO);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @PutMapping("/me/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDTO>> updateMyCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody CartItemDTO cartItemDTO) {
        CartDTO cart = cartService.updateMyCartItem(authentication.getName(), cartItemId, cartItemDTO);
        return ResponseEntity.ok(ApiResponse.success(cart));
    }

    @DeleteMapping("/me/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartDTO>> removeMyCartItem(
            Authentication authentication,
            @PathVariable Long cartItemId) {
        CartDTO cart = cartService.removeMyCartItem(authentication.getName(), cartItemId);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cart));
    }

    @DeleteMapping("/me/clear")
    public ResponseEntity<ApiResponse<Void>> clearMyCart(Authentication authentication) {
        cartService.clearMyCart(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Cart cleared successfully", null));
    }
}
