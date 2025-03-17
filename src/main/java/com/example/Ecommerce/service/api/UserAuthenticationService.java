package com.example.Ecommerce.service.api;

import com.example.Ecommerce.dto.LoginRequestDTO;
import com.example.Ecommerce.dto.UserRequestDTO;
import com.example.Ecommerce.dto.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserAuthenticationService {
    ResponseEntity<String> createUser(UserRequestDTO userRequestDTO);

    ResponseEntity<UserResponseDTO> getUserDetails(Long userId);

    ResponseEntity<String> deleteUserById(Long userId);

    ResponseEntity<String> userServiceAuthentication(LoginRequestDTO loginRequestDTO);
}
