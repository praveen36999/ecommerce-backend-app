package com.example.Ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

//To transfer data from database to server
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDTO {

    private List<CategoryRequestDTO> categoryRequestDTOList;

    int pageNumber;
    int pageSize;
    long totalElements;
    int totalPages;
    boolean lastPage;

}
