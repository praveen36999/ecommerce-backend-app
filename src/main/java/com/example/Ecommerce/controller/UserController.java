package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.CategoryConstants;
import com.example.Ecommerce.config.ProductConstants;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import com.example.Ecommerce.dto.ProductResponseDTO;
import com.example.Ecommerce.dto.SubCategoryUserResponseDTO;
import com.example.Ecommerce.service.api.CategoryService;
import com.example.Ecommerce.service.api.ProductService;
import com.example.Ecommerce.service.api.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.Ecommerce.config.CategoryConstants.DEFAULT_SORT_BY;
import static com.example.Ecommerce.config.CategoryConstants.DEFAULT_SORT_ORDER;

@RestController
@RequestMapping("/api/public")
public class UserController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SubCategoryService subCategoryService;

    @Autowired
    private ProductService productService;

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

    //retrieve all products by subcategory
    @GetMapping("categories/{subCategoryId}/products")
    private ResponseEntity<ProductResponseDTO> getAllProductsBySubCategory(
            @RequestParam(name = "pageNumber",defaultValue = ProductConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = ProductConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = ProductConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = ProductConstants.DEFAULT_SORT_ORDER) String sortOrder,
            @PathVariable Long subCategoryID
    ){
        return productService.getAllProductsBySubCategoryId(pageNumber,pageSize,sortBy,sortOrder,subCategoryID);
    }

    // get products by keyword
    @GetMapping("categories/products/{keyword}")
    public ResponseEntity<ProductResponseDTO> getAllProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber",defaultValue = ProductConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = ProductConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = ProductConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = ProductConstants.DEFAULT_SORT_ORDER) String sortOrder){
        return productService.getAllProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
    }

}
