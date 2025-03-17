package com.example.Ecommerce.dto;

import com.example.Ecommerce.dao.model.Address;
import com.example.Ecommerce.dao.model.UserRole;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {
    private Long userId;

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    @Column(name = "email_address")
    @Email(message = "Enter a valid email id only numbers and alphabets allowed. sample format xxx123@gmail.com")
    private String emailAddress;

    private Set<Address> address;

    private Set<UserRole> userRole;
}
