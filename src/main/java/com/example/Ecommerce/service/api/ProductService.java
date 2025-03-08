package com.example.Ecommerce.service.api;

import com.example.Ecommerce.dto.ProductRequestDTO;
import com.example.Ecommerce.dto.ProductResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    ResponseEntity<String> addProduct(ProductRequestDTO productRequestDTO, Long subCategoryId);




    ResponseEntity<ProductResponseDTO> getAllProductsBySubCategoryId(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long subCategoryId);

    ResponseEntity<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseEntity<ProductResponseDTO> getAllProductsByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ResponseEntity<String> updateProductDetails(Long productId, ProductRequestDTO productRequestDTO);

    ResponseEntity<String> deleteProductById(Long productId);
}
