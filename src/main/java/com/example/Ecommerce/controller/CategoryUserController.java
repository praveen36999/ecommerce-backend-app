package com.example.Ecommerce.controller;

import com.example.Ecommerce.dto.CategoryResponseDTO;
import com.example.Ecommerce.service.api.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class CategoryUserController {
    @Autowired
    private CategoryService categoryService;


//    @GetMapping(path = "/categories")
//    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories(){
//        return  categoryService.getAllCategories();
//    }

    @GetMapping(path = "{categoryId}/subcategories")
    public String getAllSubCategories(){
        return "from getSubCategories";
    }

}
