package com.example.Ecommerce.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    private Long productId;

    @NotBlank(message = "Product name cannot not be null or empty")
    @Size(min = 3, max = 50 , message = "Product name should have length between three and fifty characters")
    @Pattern(regexp = "^[A-Za-z0-9\s]+$", message = "Product name should contain only Alphabets, Digits and Space")
    private String productName;

    @NotNull(message = "Product price cannot not be null")
    @Min(value = 1,message = "Product Price cannot be 0 or Negative")
    private double price;

    @NotNull(message = "Product Quantity cannot not be null")
    @Min(value = 0,message = "Product Quantity cannot be Negative")
    private int quantity;

    @NotBlank(message = "Product Description cannot not be null or empty")
    @Size(min = 30, message = "Product Description should have length minimum thirty characters")
    private String description;

    private String image;

    private boolean isAvailable;

    @Min(value = 0,message = "Product Quantity cannot be Negative")
    private double discount;

    @Min(value = 0,message = "Discounted Price cannot be Negative")
    private double discountedPrice;



}
