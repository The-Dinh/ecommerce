package com.ecommerce.service.impl;

import com.ecommerce.dto.AuthResponse;
import com.ecommerce.dto.LoginRequest;
import com.ecommerce.dto.RegisterRequest;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;
import com.ecommerce.enumtype.Role;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.JwtService;
import com.ecommerce.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .role(Role.CUSTOMER)
                .build();

        User savedUser = userRepository.save(user);
        createCartForUserIfMissing(savedUser);

        return buildAuthResponse(savedUser, "Register successfully", null);
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new BadRequestException("Invalid email or password");
        }

        User user = getUserByEmail(request.getEmail());
        String accessToken = jwtService.generateAccessToken(user);

        return buildAuthResponse(user, "Login successfully", accessToken);
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found with email: " + email));
    }

    private AuthResponse buildAuthResponse(User user, String message, String accessToken) {
        return AuthResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .message(message)
                .tokenType(accessToken == null ? null : "Bearer")
                .accessToken(accessToken)
                .build();
    }

    private void createCartForUserIfMissing(User user) {
        if (cartRepository.findByUserId(user.getId()).isPresent()) {
            return;
        }

        Cart cart = Cart.builder()
                .user(user)
                .build();
        cartRepository.save(cart);
    }
}
