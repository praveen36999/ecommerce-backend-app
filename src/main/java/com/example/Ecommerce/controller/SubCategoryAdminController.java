package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.SubCategoryConstants;
import com.example.Ecommerce.dao.model.SubCategory;
import com.example.Ecommerce.dto.SubCategoryRequestDTO;
import com.example.Ecommerce.dto.SubCategoryResponseDTO;
import com.example.Ecommerce.service.api.SubCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class SubCategoryAdminController {

    @Autowired
    private SubCategoryService subCategoryService;

    // add subcategory in category
    @PostMapping("/categories/{categoryId}/addSubCategory")
    public ResponseEntity<String> createSubCategory(@Valid @RequestBody SubCategoryRequestDTO subCategoryRequestDTO,
                                                    @PathVariable Long categoryId){
        return subCategoryService.addSubCategory(subCategoryRequestDTO,categoryId);
    }


    //retrieve all the subcategories
    @GetMapping("/categories/subCategories")
    public ResponseEntity<SubCategoryResponseDTO> getAllSubCategories(
            @RequestParam(name = "pageNumber",defaultValue = SubCategoryConstants.DEFAULT_PAGE_NUMBER)Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = SubCategoryConstants.DEFAULT_PAGE_SIZE)Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = SubCategoryConstants.DEFAULT_SORT_BY)String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = SubCategoryConstants.DEFAULT_SORT_ORDER)String sortOrder)
    {
        return subCategoryService.getAllSubCategories(pageNumber,pageSize,sortBy,sortOrder);
    }

    //delete subcategory by name or id
    @DeleteMapping("/categories/{subCategoryId}/deleteSubCategory")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Long subCategoryId){
        return subCategoryService.deleteSubCategoryById(subCategoryId);
    }

    //update subcategory by id
    @PutMapping("/categories/{subCategoryId}/update")
    public ResponseEntity<String> updateSubCategoryName(@PathVariable Long subCategoryId,
                                                        @Valid @RequestBody SubCategoryRequestDTO subCategoryRequestDTO){
        return subCategoryService.updateSubCategoryName(subCategoryId,subCategoryRequestDTO);
    }
}
