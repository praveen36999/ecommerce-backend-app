package com.example.Ecommerce.controller;

import com.example.Ecommerce.config.ProductConstants;
import com.example.Ecommerce.dto.ProductRequestDTO;
import com.example.Ecommerce.dto.ProductResponseDTO;
import com.example.Ecommerce.dto.SubCategoryRequestDTO;
import com.example.Ecommerce.service.api.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/product")
public class ProductAdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductAdminController.class);
    @Autowired
    private ProductService productService;

    //add product in subcategory
    @PostMapping("/{subCategoryId}/addProduct")
    public ResponseEntity<String> createProduct(@Valid @RequestBody ProductRequestDTO productRequestDTO,
                                                    @PathVariable Long subCategoryId){
        LOGGER.info("Received request to create new product by subcategoryId :{}"+" with " +
                "productName as : {}",subCategoryId,productRequestDTO.getProductName());
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
        LOGGER.info("Received request to get all the products with page details as"+
                " pageNumber: {},pageSize: {},sortBy: {},sortOrder: {}", pageNumber,pageSize,sortBy,sortOrder);
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
        LOGGER.info("Received request to get all the products by subcategoryId :{}" +" with page details as"+
                " pageNumber: {},pageSize: {},sortBy: {},sortOrder: {}",subCategoryId, pageNumber,pageSize,sortBy,sortOrder);
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
        LOGGER.info("Received request to get all the products has keyword : {}" +" with page details as"+
                " pageNumber: {},pageSize: {},sortBy: {},sortOrder: {}",keyword, pageNumber,pageSize,sortBy,sortOrder);
        return productService.getAllProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
    }



    @PutMapping("/{productId}/update")
    public ResponseEntity<String> updateProductName(@PathVariable Long productId,
                                                        @Valid @RequestBody ProductRequestDTO productRequestDTO){
        LOGGER.info("Received request to update an existing product by productId as : {} "+
                "and new updated productName as productName as : {}",productId,productRequestDTO.getProductName());
        return productService.updateProductDetails(productId,productRequestDTO);
    }

    @DeleteMapping("/{productId}/deleteProduct")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId){
        LOGGER.info("Received request to delete an existing product with " +
                "productId as : {}",productId);
        return productService.deleteProductById(productId);
    }
}
