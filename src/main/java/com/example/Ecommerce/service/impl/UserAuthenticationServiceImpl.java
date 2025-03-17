package com.example.Ecommerce.service.impl;

import com.example.Ecommerce.dao.UserRepository;
import com.example.Ecommerce.dao.model.User;
import com.example.Ecommerce.dto.LoginRequestDTO;
import com.example.Ecommerce.dto.UserRequestDTO;
import com.example.Ecommerce.dto.UserResponseDTO;
import com.example.Ecommerce.service.api.UserAuthenticationService;
import com.example.Ecommerce.service.exception.DuplicateEntryException;
import com.example.Ecommerce.service.exception.ResourceNotFoundException;
import com.example.Ecommerce.service.security.JwtUtilService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    JwtUtilService jwtUtilService;

    @Override
    public ResponseEntity<String> createUser(UserRequestDTO userRequestDTO) {
        LOGGER.info("Attempting to create user with email address: {}", userRequestDTO.getEmailAddress());
        boolean userExists = userRepository.existsByEmailAddress(userRequestDTO.getEmailAddress());

        if(userExists) {
            LOGGER.warn("User with email {} already exists", userRequestDTO.getEmailAddress());
            throw new DuplicateEntryException("User", userRequestDTO.getEmailAddress());
        }

        User user = modelMapper.map(userRequestDTO,User.class);
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPassword()));
        user.getUserRoles().addAll(userRequestDTO.getUserRole());
        userRepository.save(user);

        LOGGER.info("User created successfully with email address: {}", userRequestDTO.getEmailAddress());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully.Try logging in with "+user.getEmailAddress());
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserDetails(Long userId) {
        LOGGER.info("Fetching user details for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() ->  {
                    LOGGER.error("User with ID {} not found", userId);
                    return new ResourceNotFoundException("user", userId);
                });

        UserResponseDTO userResponseDTO = modelMapper.map(user,UserResponseDTO.class);
        LOGGER.info("Returning user details for userId: {}", userId);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @Override
    public ResponseEntity<String> deleteUserById(Long userId) {
        LOGGER.info("Attempting to delete user with userId: {}", userId);

        boolean isUserExists = userRepository.existsByUserId(userId);
        if(!isUserExists) {
            LOGGER.error("User with ID {} not found for deletion", userId);
            throw new ResourceNotFoundException("user", userId);
        }
        userRepository.deleteByUserId(userId);
        LOGGER.info("User with ID {} deleted successfully", userId);
        return ResponseEntity.status(HttpStatus.OK).body("user deleted successfully");
    }

    @Override
    public ResponseEntity<String> userServiceAuthentication(LoginRequestDTO loginRequestDTO) {
        LOGGER.info("Attempting to authenticate user with email address: {}", loginRequestDTO.getEmailAddress());
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmailAddress(), loginRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            String token = jwtUtilService.generateToken(loginRequestDTO.getEmailAddress());
            LOGGER.info("User authenticated successfully with email: {}", loginRequestDTO.getEmailAddress());
            return ResponseEntity.status(HttpStatus.OK).body("User Verified"+ "/n" + "Token : " +token);
        }
        LOGGER.warn("Authentication failed for user with email address: {}", loginRequestDTO.getEmailAddress());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad Credentials");
    }
}
