package com.example.Ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// to transfer data from client to server
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequestDTO {

    private Long categoryId;
    @NotBlank(message = "Category name cannot not be null or empty")
    @Size(min = 3, max = 50 , message = "Category name should have length between three and fifty")
    @Pattern(regexp = "^[A-Za-z\s]+$", message = "Category name should contain only Alphabets and Space")
    private String categoryName;


}
