package com.ecommerce.dto.auth;

import com.ecommerce.enumtype.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Long id;

    private String fullName;

    private String email;

    private Role role;

    private String message;

    private String tokenType;

    private String accessToken;
}

