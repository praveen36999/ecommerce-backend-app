package com.example.Ecommerce.service.impl;

import com.example.Ecommerce.dao.ProductRepository;
import com.example.Ecommerce.dao.SubCategoryRepository;
import com.example.Ecommerce.dao.model.Product;
import com.example.Ecommerce.dao.model.SubCategory;
import com.example.Ecommerce.dto.ProductRequestDTO;
import com.example.Ecommerce.dto.ProductResponseDTO;
import com.example.Ecommerce.service.api.ProductService;
import com.example.Ecommerce.service.exception.DuplicateCategoryException;
import com.example.Ecommerce.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private ProductRequestDTO productRequestDTO;

    @Autowired
    private ProductResponseDTO productResponseDTO;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<String> addProduct(ProductRequestDTO productRequestDTO, Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findBySubCategoryId(subCategoryId)
                .orElseThrow(() -> new ResourceNotFoundException("SubCategory",subCategoryId));


        Product product = modelMapper.map(productRequestDTO,Product.class);

        //check if already product exist under targeted subcategory
        boolean isProductExists = productRepository
                .existsByProductName(product.getProductName());
        if(isProductExists){
            throw new DuplicateCategoryException("Product",product.getProductName());
        }

        boolean isAvailable = product.getQuantity() > 0 ? true : false;
        if(isAvailable){
            product.setAvailable(true);
        }
        if(product.getDiscount() > 0){
            double productDiscount = product.getDiscount();
            double productPrice = product.getPrice();
            double productDiscountedPrice = productPrice - (productPrice * productDiscount / 100);
            product.setDiscountedPrice(productDiscountedPrice);
        }

        subCategory.addProduct(product);
        product.setSubCategory(subCategory);

        productRepository.save(product);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("New Product got created successfully in %s",subCategory.getSubCategoryName()));

    }

    @Override
    public ResponseEntity<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Sort sortByOrder;
        if(sortOrder.equals("default")){
            sortByOrder = Sort.unsorted();
        }else{
            sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
        }
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Product> pageResult = productRepository.findAll(pageDetails);
        List<Product> productList = pageResult.getContent();
         if(productList.isEmpty()){
             throw new ResourceNotFoundException("Products");
         }
         List<ProductRequestDTO> productRequestDTOList = productList.stream()
                 .map(product -> modelMapper.map(product,ProductRequestDTO.class)).toList();
         productResponseDTO.setProductRequestDTOList(productRequestDTOList);
         productResponseDTO.setPageNumber(pageResult.getNumber());
         productResponseDTO.setPageSize(pageResult.getSize());
         productResponseDTO.setTotalElements(pageResult.getTotalElements());
         productResponseDTO.setTotalPages(pageResult.getTotalPages());
         productResponseDTO.setLastPage(pageResult.isLast());
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @Override
    public ResponseEntity<ProductResponseDTO> getAllProductsByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){
        Sort sortByOrder;
        if(sortOrder.equals("default")){
            sortByOrder = Sort.unsorted();
        }else{
            sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

        }
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Product> pageResult = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%",pageDetails);
        if(!pageResult.hasContent()){
            throw new ResourceNotFoundException();
        }
        List<ProductRequestDTO> productRequestDTOList = pageResult.stream()
                .map(product ->modelMapper.map(product,ProductRequestDTO.class)).toList();

        productResponseDTO.setProductRequestDTOList(productRequestDTOList);
        productResponseDTO.setProductRequestDTOList(productRequestDTOList);
        productResponseDTO.setProductRequestDTOList(productRequestDTOList);
        productResponseDTO.setPageNumber(pageResult.getNumber());
        productResponseDTO.setPageSize(pageResult.getSize());
        productResponseDTO.setTotalElements(pageResult.getTotalElements());
        productResponseDTO.setTotalPages(pageResult.getTotalPages());
        productResponseDTO.setLastPage(pageResult.isLast());
        return ResponseEntity.status(HttpStatus.FOUND).body(productResponseDTO);
    }

    @Override
    public ResponseEntity<String> updateProductDetails(Long productId, ProductRequestDTO productRequestDTO) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        product.setAvailable(productRequestDTO.isAvailable());
        product.setProductName(productRequestDTO.getProductName());
        product.setDescription(productRequestDTO.getDescription());
        product.setImage(productRequestDTO.getImage());
        double productDiscount = productRequestDTO.getDiscount();
        double productPrice = productRequestDTO.getPrice();
        if(productDiscount > 0){
            double productDiscountedPrice = productPrice - (productPrice * productDiscount / 100);
            product.setDiscountedPrice(productDiscountedPrice);
        }else{
            product.setDiscountedPrice(productPrice);
        }
        product.setPrice(productPrice);
        product.setDiscount(productDiscount);
        product.setQuantity(productRequestDTO.getQuantity());

        productRepository.save(product);

        return ResponseEntity.ok("Product got updated successfully");
    }

    @Override
    public ResponseEntity<String> deleteProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product"));
        SubCategory subCategory = product.getSubCategory();
        subCategory.getProducts().remove(product);
        subCategoryRepository.save(subCategory);
        return ResponseEntity.status(HttpStatus.OK).body("Product got deleted successfully");
    }

    @Override
    public ResponseEntity<ProductResponseDTO> getAllProductsBySubCategoryId(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long subCategoryId) {
        Sort sortByOrder;
        if(sortOrder.equals("default")){
            sortByOrder = Sort.unsorted();
        }else{
            sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
        }
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        Page<Product> pageResult = productRepository.findAll(pageDetails);
        List<Product> productList = pageResult.getContent().stream()
                .filter(product -> product.getSubCategory().getSubCategoryId().equals(subCategoryId)).toList();
        if(productList.isEmpty()){
            throw new ResourceNotFoundException("Products");
        }
        List<ProductRequestDTO> productRequestDTOList = productList.stream()
                .map(product -> modelMapper.map(product,ProductRequestDTO.class)).toList();
        productResponseDTO.setProductRequestDTOList(productRequestDTOList);
        productResponseDTO.setProductRequestDTOList(productRequestDTOList);
        productResponseDTO.setPageNumber(pageResult.getNumber());
        productResponseDTO.setPageSize(pageResult.getSize());
        productResponseDTO.setTotalElements(pageResult.getTotalElements());
        productResponseDTO.setTotalPages(pageResult.getTotalPages());
        productResponseDTO.setLastPage(pageResult.isLast());
        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }




}
