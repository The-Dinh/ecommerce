package com.ecommerce.service;

import com.ecommerce.dto.CartDTO;
import com.ecommerce.dto.CartItemDTO;

public interface CartService {

    CartDTO getMyCart(String email);

    CartDTO addItemToMyCart(String email, CartItemDTO cartItemDTO);

    CartDTO updateMyCartItem(String email, Long cartItemId, CartItemDTO cartItemDTO);

    CartDTO removeMyCartItem(String email, Long cartItemId);

    void clearMyCart(String email);
}
