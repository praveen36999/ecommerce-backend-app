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
public class CategoryResponseAdminDTO {

    private List<CategoryRequestDTO> categoryRequestDTOList;

    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;

}
