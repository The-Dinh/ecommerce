package com.ecommerce.service;

import com.ecommerce.dto.user.AdminUpdateUserRequest;
import com.ecommerce.dto.user.UpdateProfileRequest;
import com.ecommerce.dto.user.UpdateUserRoleRequest;
import com.ecommerce.dto.user.UserDTO;
import java.util.List;

public interface UserService {

    UserDTO getMyProfile(String email);

    UserDTO updateMyProfile(String email, UpdateProfileRequest request);

    UserDTO updateUser(Long id, AdminUpdateUserRequest request);

    UserDTO updateUserRole(Long id, UpdateUserRoleRequest request);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    void deleteUser(Long id);
}

