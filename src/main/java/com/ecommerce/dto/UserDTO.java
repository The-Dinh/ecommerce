package com.ecommerce.dto;

import com.ecommerce.enumtype.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;

    private String fullName;

    private String email;

    private String phone;

    private String address;

    private Role role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
