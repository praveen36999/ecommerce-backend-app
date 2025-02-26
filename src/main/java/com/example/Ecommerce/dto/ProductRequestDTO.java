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

    private Long subCategoryId;

    @NotBlank(message = "Product name cannot not be null or empty")
    @Size(min = 3, max = 50 , message = "Product name should have length between three and fifty characters")
    @Pattern(regexp = "^[A-Za-z\s]+$", message = "Product name should contain only Alphabets and Space")
    private String subCategoryName;

    @NotNull(message = "Product price cannot not be null")
    @Min(value = 1,message = "Product Price cannot be lesser than 1")
    private long price;

    @NotNull(message = "Product Quantity cannot not be null")
    @Min(value = 0,message = "Product Quantity cannot be lesser than 0")
    private int quantity;

    private boolean isAvailable;

    private float discount;

    private long discountedPrice;

    @NotBlank(message = "Product Description cannot not be null or empty")
    @Size(min = 30, message = "Product Description should have length minimum thirty characters")
    private String description;

}
