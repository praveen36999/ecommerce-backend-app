package com.example.Ecommerce.dto;

import com.example.Ecommerce.dao.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

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


    private Set<Product> products;


}
