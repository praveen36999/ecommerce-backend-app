package com.example.Ecommerce.dto;

import com.example.Ecommerce.dao.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long userId;
    private String userName;
    private String emailAddress;

    private Set<Address> address;

}
