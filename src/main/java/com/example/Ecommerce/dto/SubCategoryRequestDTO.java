package com.example.Ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryRequestDTO {

    private Long subCategoryId;

    @NotBlank(message = "Subcategory name cannot not be null or empty")
    @Size(min = 3, max = 50 , message = "Subcategory name should have length between three and fifty")
    @Pattern(regexp = "^[A-Za-z\\s&-]+$", message = "Subcategory name should contain only Alphabets and Space")
    private String subCategoryName;





}
