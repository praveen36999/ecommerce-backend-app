package com.example.Ecommerce.service.impl;

import com.example.Ecommerce.dao.ProductRepository;
import com.example.Ecommerce.dao.SubCategoryRepository;
import com.example.Ecommerce.dao.model.Product;
import com.example.Ecommerce.dao.model.SubCategory;
import com.example.Ecommerce.dto.ProductRequestDTO;
import com.example.Ecommerce.dto.ProductResponseDTO;
import com.example.Ecommerce.service.api.ProductService;
import com.example.Ecommerce.service.exception.DuplicateEntryException;
import com.example.Ecommerce.service.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

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
        LOGGER.info("Received request to add product for SubCategory ID: {}", subCategoryId);
        SubCategory subCategory = subCategoryRepository.findBySubCategoryId(subCategoryId)
                .orElseThrow(() -> {
                    LOGGER.error("Subcategory with Id: {} not found",subCategoryId);
                    return new ResourceNotFoundException("SubCategory", subCategoryId);
                });

        LOGGER.info("SubCategory with ID '{}' found: {}", subCategoryId, subCategory.getSubCategoryName());


        Product product = modelMapper.map(productRequestDTO,Product.class);

        //check if already product exist under targeted subcategory
        boolean isProductExists = productRepository
                .existsByProductName(product.getProductName());
        if(isProductExists){
            LOGGER.warn("Product with name '{}' already exists under SubCategory ID: {}", product.getProductName(), subCategoryId);
            throw new DuplicateEntryException("Product",product.getProductName());
        }

        boolean isAvailable = product.getQuantity() > 0 ? true : false;
        if(isAvailable){
            product.setAvailable(true);
            LOGGER.info("Product '{}' is available, setting availability to true", product.getProductName());
        }
        if(product.getDiscount() > 0){
            double productDiscount = product.getDiscount();
            double productPrice = product.getPrice();
            double productDiscountedPrice = productPrice - (productPrice * productDiscount / 100);
            product.setDiscountedPrice(productDiscountedPrice);
            LOGGER.info("Discount applied to Product '{}'. Original price: {}, Discounted price: {}",
                    product.getProductName(), product.getPrice(), product.getDiscountedPrice());
        }

        subCategory.addProduct(product);
        product.setSubCategory(subCategory);

                productRepository.save(product);
        LOGGER.info("Product '{}' added successfully under SubCategory '{}'", product.getProductName(), subCategory.getSubCategoryName());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("New Product got created successfully in %s",subCategory.getSubCategoryName()));

    }

    @Override
    public ResponseEntity<ProductResponseDTO> getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){

        LOGGER.info("Received request to get products with pageNumber: {}, pageSize: {}, sortBy: {}, sortOrder: {}",
                pageNumber, pageSize, sortBy, sortOrder);

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
             LOGGER.warn("No products found for the requested page and filters.");
             throw new ResourceNotFoundException("Products");
         }
        LOGGER.info("Successfully retrieved {} products.", productList.size());
         List<ProductRequestDTO> productRequestDTOList = productList.stream()
                 .map(product -> modelMapper.map(product,ProductRequestDTO.class)).toList();
         productResponseDTO.setProductRequestDTOList(productRequestDTOList);
         productResponseDTO.setPageNumber(pageResult.getNumber());
         productResponseDTO.setPageSize(pageResult.getSize());
         productResponseDTO.setTotalElements(pageResult.getTotalElements());
         productResponseDTO.setTotalPages(pageResult.getTotalPages());
         productResponseDTO.setLastPage(pageResult.isLast());

        LOGGER.info("Returning product data with page number: {} and total pages: {}", pageResult.getNumber(), pageResult.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }

    @Override
    public ResponseEntity<ProductResponseDTO> getAllProductsByKeyword(String keyword,Integer pageNumber, Integer pageSize, String sortBy, String sortOrder){

        LOGGER.info("Received request to search products with keyword: {}, pageNumber: {}, pageSize: {}, sortBy: {}, sortOrder: {}",
                keyword, pageNumber, pageSize, sortBy, sortOrder);


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
            LOGGER.warn("No products found with keyword: {}", keyword);
            throw new ResourceNotFoundException();
        }

        LOGGER.info("Found {} products with the keyword: {}", pageResult.getTotalElements(), keyword);
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

        LOGGER.info("Returning product data with page number: {} and total pages: {}", pageResult.getNumber(), pageResult.getTotalPages());

        return ResponseEntity.status(HttpStatus.FOUND).body(productResponseDTO);
    }

    @Override
    public ResponseEntity<String> updateProductDetails(Long productId, ProductRequestDTO productRequestDTO) {
        LOGGER.info("Received request to update product with ID: {}", productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> {
                    LOGGER.error("Product with ID: {} not found", productId);
                    return new ResourceNotFoundException("Product");
                });

        LOGGER.info("Updating product details for product ID: {}", productId);
        LOGGER.debug("Old Product Name: {}, New Product Name: {}", product.getProductName(), productRequestDTO.getProductName());
        LOGGER.debug("Old Price: {}, New Price: {}", product.getPrice(), productRequestDTO.getPrice());
        LOGGER.debug("Old Quantity: {}, New Quantity: {}", product.getQuantity(), productRequestDTO.getQuantity());

        product.setAvailable(productRequestDTO.isAvailable());
        product.setProductName(productRequestDTO.getProductName());
        product.setDescription(productRequestDTO.getDescription());
        product.setImage(productRequestDTO.getImage());

        double productDiscount = productRequestDTO.getDiscount();
        double productPrice = productRequestDTO.getPrice();
        if(productDiscount > 0){
            double productDiscountedPrice = productPrice - (productPrice * productDiscount / 100);
            product.setDiscountedPrice(productDiscountedPrice);
            LOGGER.debug("Calculated discounted price: {}", productDiscountedPrice);
        }else{
            product.setDiscountedPrice(productPrice);
        }
        product.setPrice(productPrice);
        product.setDiscount(productDiscount);
        product.setQuantity(productRequestDTO.getQuantity());

        productRepository.save(product);
        LOGGER.info("Product with ID: {} updated successfully", productId);

        return ResponseEntity.ok("Product got updated successfully");
    }

    @Override
    public ResponseEntity<String> deleteProductById(Long productId) {
        LOGGER.info("Received request to delete product with ID: {}", productId);
        Product product = productRepository.findById(productId)
                .orElseThrow( () -> {
                        LOGGER.error("Product with ID: {} not found", productId);
        return new ResourceNotFoundException("Product not found with ID: " + productId);
                });

        SubCategory subCategory = product.getSubCategory();
        LOGGER.debug("Product with ID: {} is associated with subCategory: {}", productId, subCategory.getSubCategoryName());

        subCategory.getProducts().remove(product);
        subCategoryRepository.save(subCategory);

        LOGGER.info("Product with ID: {} deleted successfully from subCategory: {}", productId, subCategory.getSubCategoryName());

        return ResponseEntity.status(HttpStatus.OK).body("Product got deleted successfully");
    }

    @Override
    public ResponseEntity<ProductResponseDTO> getAllProductsBySubCategoryId(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder, Long subCategoryId) {
        LOGGER.info("Received request to get all products for subCategoryId: {} with page number: {} and page size: {}", subCategoryId, pageNumber, pageSize);

        Sort sortByOrder;
        if(sortOrder.equals("default")){
            sortByOrder = Sort.unsorted();
            LOGGER.debug("Sorting products by {} in {} order",sortBy,sortOrder);
        }else{
            sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();
        }
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByOrder);
        LOGGER.debug("Pageable details: pageNumber={}, pageSize={}, sortByOrder={}",pageNumber,pageSize,sortByOrder);

        Page<Product> pageResult = productRepository.findAll(pageDetails);
        LOGGER.debug("Fetched products page: {} with {} products", pageResult.getNumber(), pageResult.getSize());

        List<Product> productList = pageResult.getContent().stream()
                .filter(product -> product.getSubCategory().getSubCategoryId().equals(subCategoryId)).toList();
        if(productList.isEmpty()){
            LOGGER.warn("No products found for subCategoryId: {}", subCategoryId);
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

        LOGGER.info("Returning product data with page number: {} and total pages: {}", pageResult.getNumber(), pageResult.getTotalPages());


        return ResponseEntity.status(HttpStatus.OK).body(productResponseDTO);
    }




}
