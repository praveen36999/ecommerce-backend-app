package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.CategoryConstants;
import com.example.Ecommerce.dto.CategoryRequestDTO;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import com.example.Ecommerce.service.api.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.Ecommerce.config.CategoryConstants.*;

@RestController
@RequestMapping("/api/admin")
public class CategoryAdminController {

        @Autowired
        private CategoryService categoryService;


    // It gets all the categories from DB
        @GetMapping(produces = "application/json",path = "/categories")
        public ResponseEntity<CategoryResponseDTO> getAllCategories(
                @RequestParam(name = "pageNumber",defaultValue = CategoryConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                @RequestParam(name = "pageSize", defaultValue = CategoryConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
                @RequestParam(name= "sortBy",defaultValue = DEFAULT_SORT_BY)String sortBy,
                @RequestParam(name = "sortOrder",defaultValue = DEFAULT_SORT_ORDER)String sortOrder)
        {

            return  categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);
        }


        //It creates a new category in DB
        @PostMapping(consumes = "application/json", path = "/categories/addCategory")
        public ResponseEntity<String> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO){
            return categoryService.createCategory(categoryRequestDTO);
        }

        //It checks and deletes the given category
        @DeleteMapping(path = "/categories/categoryname/{categoryName}")
        public ResponseEntity<String>  deleteCategoryByName(@Valid @PathVariable String categoryName){
          return  categoryService.deleteCategory(categoryName);
        }

        //It checks and deletes the given category
        @DeleteMapping(path = "/categories/categoryid/{categoryId}")
        public ResponseEntity<String>  deleteCategoryByID(@PathVariable Long categoryId){
            return  categoryService.deleteCategory(categoryId);
        }

        //It updates the existing category name
        @PutMapping(path = "/categories/{categoryId}/updateCategory")
        public ResponseEntity<String> updateCategoryName(@PathVariable Long categoryId
                                 ,@Valid @RequestBody CategoryRequestDTO categoryRequestDTO)

        {
            categoryService.updateCategoryName(categoryId,categoryRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
        }


}
