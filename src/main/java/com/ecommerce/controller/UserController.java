package com.ecommerce.controller;

import com.ecommerce.dto.UserDTO;
import com.ecommerce.exception.ApiResponse;
import com.ecommerce.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // TODO: Inject UserService

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        // TODO: Call service
        List<UserDTO> users = Collections.emptyList();
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable Long id) {
        // TODO: Call service
        UserDTO user = new UserDTO();
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        // TODO: Call service
        UserDTO createdUser = new UserDTO();
        return ResponseEntity.status(201).body(ApiResponse.created(createdUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        // TODO: Call service
        UserDTO updatedUser = new UserDTO();
        return ResponseEntity.ok(ApiResponse.success(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        // TODO: Call service
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
