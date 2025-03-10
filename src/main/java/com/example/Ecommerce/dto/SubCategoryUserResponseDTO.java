package com.example.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryUserResponseDTO {
    private List<SubCategoryRequestDTO> subCategoryRequestDTOList;
}
