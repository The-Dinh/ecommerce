package com.ecommerce.service.impl;

import com.ecommerce.dto.OrderDTO;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = User.builder()
                .fullName(userDTO.getFullName())
                .email(userDTO.getEmail())
                .passwordHash("123456")
                .phone(userDTO.getPhone())
                .address(userDTO.getAddress())
                .role(userDTO.getRole())
                .build();
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser); 
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setFullName(userDTO.getFullName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        user.setAddress(userDTO.getAddress());
        user.setRole(userDTO.getRole());
        User updatedUser = userRepository.save(user);
        return mapToDTO(updatedUser); 
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return mapToDTO(user); 
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
