package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.SubCategoryConstants;
import com.example.Ecommerce.dao.model.SubCategory;
import com.example.Ecommerce.dto.SubCategoryRequestDTO;
import com.example.Ecommerce.dto.SubCategoryResponseDTO;
import com.example.Ecommerce.service.api.SubCategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class SubCategoryAdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryAdminController.class);
    @Autowired
    private SubCategoryService subCategoryService;

    // add subcategory in category
    @PostMapping("/categories/{categoryId}/addSubCategory")
    public ResponseEntity<String> createSubCategory(@Valid @RequestBody SubCategoryRequestDTO subCategoryRequestDTO,
                                                    @PathVariable Long categoryId){
        LOGGER.info("Received request to create new subcategory by categoryId :{}"+" with " +
                "subCategoryName as : {}",categoryId,subCategoryRequestDTO.getSubCategoryName());
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
        LOGGER.info("Received request to get all the subcategories with page details as"+
                " pageNumber: {},pageSize: {},sortBy: {},sortOrder: {}", pageNumber,pageSize,sortBy,sortOrder);
        return subCategoryService.getAllSubCategories(pageNumber,pageSize,sortBy,sortOrder);
    }

    //delete subcategory by name or id
    @DeleteMapping("/categories/{subCategoryId}/deleteSubCategory")
    public ResponseEntity<String> deleteSubCategory(@PathVariable Long subCategoryId){
        LOGGER.info("Received request to delete an existing subcategory with " +
                "subCategoryId as : {}",subCategoryId);
        return subCategoryService.deleteSubCategoryById(subCategoryId);
    }

    //update subcategory by id
    @PutMapping("/categories/{subCategoryId}/update")
    public ResponseEntity<String> updateSubCategoryName(@PathVariable Long subCategoryId,
                                                        @Valid @RequestBody SubCategoryRequestDTO subCategoryRequestDTO){
        LOGGER.info("Received request to update an existing subcategory with " + "subCategoryId as : {} " +
                "and new updated subCategoryName as subCategoryName as : {}",subCategoryId,subCategoryRequestDTO.getSubCategoryName());
        return subCategoryService.updateSubCategoryName(subCategoryId,subCategoryRequestDTO);
    }
}
