package com.ecommerce.service;

import com.ecommerce.dto.UserDTO;
import java.util.List;

public interface UserService {

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Long id, UserDTO userDTO);

    UserDTO getUserById(Long id);

    List<UserDTO> getAllUsers();

    void deleteUser(Long id);
}
