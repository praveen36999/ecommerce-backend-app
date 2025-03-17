package com.example.Ecommerce.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank
    @Column(name = "email_address")
    @Email(message = "Enter a valid email id only numbers and alphabets allowed. sample format xxx123@gmail.com")
    private String emailAddress;

    @NotBlank
    private String password;
}
