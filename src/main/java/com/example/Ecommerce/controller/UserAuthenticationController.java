package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.LoginRequestDTO;
import com.example.Ecommerce.dto.UserRequestDTO;
import com.example.Ecommerce.dto.UserResponseDTO;
import com.example.Ecommerce.service.api.UserAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/user")

public class UserAuthenticationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationController.class);
    @Autowired
    UserAuthenticationService userAuthenticationService;


    //creating a new user
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody UserRequestDTO userRequestDTO){
        LOGGER.info("Received request to create new user with " +
                "userName as : {}  and email address as : {}"+userRequestDTO.getUserName(),userRequestDTO.getEmailAddress());
     return userAuthenticationService.createUser(userRequestDTO);
    }


    @PostMapping("/login")
    public ResponseEntity<String> loginExistingUser(@RequestBody LoginRequestDTO loginRequestDTO){
        LOGGER.info("Received login with email as :{}", loginRequestDTO.getEmailAddress());
        return userAuthenticationService.userServiceAuthentication(loginRequestDTO);
    }


    @GetMapping("{userId}/userinfo")
    public  ResponseEntity<UserResponseDTO> getUserDetails(@PathVariable Long userId){
        LOGGER.info("Received request to get user details by userId: {}",userId);
        return userAuthenticationService.getUserDetails(userId);
    }

    @DeleteMapping("delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId){
        LOGGER.info("Received request to delete existing user details by userId: {}",userId);
        return userAuthenticationService.deleteUserById(userId);
    }


}
