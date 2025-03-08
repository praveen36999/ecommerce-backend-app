package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.ProductConstants;
import com.example.Ecommerce.dto.ProductRequestDTO;
import com.example.Ecommerce.dto.ProductResponseDTO;
import com.example.Ecommerce.dto.SubCategoryRequestDTO;
import com.example.Ecommerce.service.api.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    //add product in subcategory
    @PostMapping("/{subCategoryId}/addProduct")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                    @PathVariable Long subCategoryId){
        return productService.addProduct(productRequestDTO,subCategoryId);
    }
    //retrieve all products
    @GetMapping("/")
    public ResponseEntity<ProductResponseDTO> getAllProducts(
            @RequestParam(name = "pageNumber",defaultValue = ProductConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = ProductConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = ProductConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = ProductConstants.DEFAULT_SORT_ORDER) String sortOrder
    ){
        return  productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder);
    }
    //retrieve all products by subcategory
    @GetMapping("/bySubCategory/{subCategoryId}/")
    public ResponseEntity<ProductResponseDTO> getAllProductsBySubCategory(
     @RequestParam(name = "pageNumber",defaultValue = ProductConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
     @RequestParam(name = "pageSize",defaultValue = ProductConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
     @RequestParam(name = "sortBy",defaultValue = ProductConstants.DEFAULT_SORT_BY) String sortBy,
     @RequestParam(name = "sortOrder",defaultValue = ProductConstants.DEFAULT_SORT_ORDER) String sortOrder,
     @PathVariable Long subCategoryId
    ){
        return productService.getAllProductsBySubCategoryId(pageNumber,pageSize,sortBy,sortOrder,subCategoryId);
    }
    // get products by keyword
    @GetMapping("/search/{keyword}")
    public ResponseEntity<ProductResponseDTO> getAllProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber",defaultValue = ProductConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = ProductConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
            @RequestParam(name = "sortBy",defaultValue = ProductConstants.DEFAULT_SORT_BY) String sortBy,
            @RequestParam(name = "sortOrder",defaultValue = ProductConstants.DEFAULT_SORT_ORDER) String sortOrder){
        return productService.getAllProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
    }

    @PutMapping("/{productId}/update")
    public ResponseEntity<String> updateSubCategoryName(@PathVariable Long productId,
                                                        @Valid @RequestBody ProductRequestDTO productRequestDTO){
        return productService.updateProductDetails(productId,productRequestDTO);
    }

    @DeleteMapping("/{productId}/deleteProduct")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        return productService.deleteProductById(productId);
    }
}
