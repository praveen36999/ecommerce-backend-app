package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.CategoryConstants;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import com.example.Ecommerce.dto.SubCategoryUserResponseDTO;
import com.example.Ecommerce.service.api.CategoryService;
import com.example.Ecommerce.service.api.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Ecommerce.config.CategoryConstants.DEFAULT_SORT_BY;
import static com.example.Ecommerce.config.CategoryConstants.DEFAULT_SORT_ORDER;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    //user can view all the categories
    @GetMapping(produces = "application/json",path = "/categories")
    public ResponseEntity<CategoryResponseDTO> getAllCategories(
            @RequestParam(name = "pageNumber",defaultValue = CategoryConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = CategoryConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(name= "sortBy",defaultValue = DEFAULT_SORT_BY)String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = DEFAULT_SORT_ORDER)String sortOrder)
    {

        return  categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
    }

    //retrieve all the subcategories by category
    @GetMapping("/categories/{categoryId}/subCategories")
    public ResponseEntity<SubCategoryUserResponseDTO> getSubCategoriesByCategory(@PathVariable Long categoryId){
        return subCategoryService.getSubCategoriesByCategory(categoryId);
    }

}
