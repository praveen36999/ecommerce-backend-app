package com.example.Ecommerce.service.api;

import com.example.Ecommerce.dto.CategoryRequestDTO;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {


    // it checks whether diff  category exist with same name else it updates


    // it checks whether diff  category exist with same name else it updates


    ResponseEntity<String> createCategory(CategoryRequestDTO categoryRequestDTO);

    ResponseEntity<String> deleteCategory(String categoryName);

    ResponseEntity<String>  deleteCategory(Long categoryId);

    ResponseEntity<CategoryResponseDTO> getAllCategories(int pageNumber,int PageSize,String sortBy,String sortOrder);

    ResponseEntity<String> updateCategoryName(Long categoryIdDTO, CategoryRequestDTO categoryRequestDTO);

    ;
}
