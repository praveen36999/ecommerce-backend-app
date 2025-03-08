package com.example.Ecommerce.service.api;

import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dao.model.SubCategory;
import com.example.Ecommerce.dto.SubCategoryRequestDTO;
import com.example.Ecommerce.dto.SubCategoryResponseDTO;
import com.example.Ecommerce.dto.SubCategoryUserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface SubCategoryService {
    public  ResponseEntity<String> addSubCategory(SubCategoryRequestDTO subCategoryRequestDTO,Long categoryId);

    ResponseEntity<String> deleteSubCategoryByCategory(Category Category);

    ResponseEntity<String> updateSubCategoryName(Long subCategoryId,SubCategoryRequestDTO subCategoryRequestDTO);

    ResponseEntity<SubCategoryResponseDTO> getAllSubCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    ResponseEntity<SubCategoryUserResponseDTO> getSubCategoriesByCategory(Long categoryId);

    ResponseEntity<String> deleteSubCategoryById(Long subCategoryId);
}
