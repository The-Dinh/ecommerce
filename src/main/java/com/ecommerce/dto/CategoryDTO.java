package com.ecommerce.dto;

import java.time.LocalDateTime;

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
public class CategoryDTO {
    // TODO: Add fields (id, name, description, createdAt)
    private Long id;

    private String name;

    private String description;

    private LocalDateTime createdAt;
}
