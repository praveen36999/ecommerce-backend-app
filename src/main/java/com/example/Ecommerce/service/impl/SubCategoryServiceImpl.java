package com.example.Ecommerce.service.impl;

import com.example.Ecommerce.dao.CategoryRepository;
import com.example.Ecommerce.dao.SubCategoryRepository;
import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dao.model.SubCategory;
import com.example.Ecommerce.dto.SubCategoryRequestDTO;
import com.example.Ecommerce.dto.SubCategoryResponseDTO;
import com.example.Ecommerce.dto.SubCategoryUserResponseDTO;
import com.example.Ecommerce.service.api.SubCategoryService;
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
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubCategoryServiceImpl.class);
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubCategoryResponseDTO subCategoryResponseDTO;

    @Autowired
    private SubCategoryUserResponseDTO subCategoryUserResponseDTO;
    @Override
    public ResponseEntity<String> addSubCategory(SubCategoryRequestDTO subCategoryRequestDTO,Long categoryId) {
        LOGGER.info("Received request to add a new subcategory with name '{}' under categoryId: {}",
                subCategoryRequestDTO.getSubCategoryName(), categoryId);

        //check valid category exist
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> {
                    LOGGER.error("Category with ID {} not found", categoryId);
                    return new ResourceNotFoundException("Category", categoryId);
                });
        //mapping subcategoryDto to subcategory
        SubCategory subCategory = modelMapper.map(subCategoryRequestDTO,SubCategory.class);
        LOGGER.info("Mapped SubCategory DTO to SubCategory entity: {}", subCategory);

        //check if already subcategory exist under targeted category
        boolean isSubCategoryExists = subCategoryRepository
                .existsBySubCategoryName(subCategory.getSubCategoryName());
        if(isSubCategoryExists){
            LOGGER.warn("Subcategory '{}' already exists", subCategory.getSubCategoryName());
            throw new DuplicateEntryException("Subcategory",subCategory.getSubCategoryName());
        }
        try {
            subCategory.setCategory(category);
            category.addSubCategory(subCategory);
            subCategoryRepository.save(subCategory);
            LOGGER.info("Successfully added subcategory '{}' to category '{}'", subCategory.getSubCategoryName(), category.getCategoryName());
        }catch(Exception e){
            LOGGER.error("Error occurred while adding subcategory '{}' to category '{}': {}", subCategory.getSubCategoryName(), category.getCategoryName(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        LOGGER.info("Subcategory with subcategory name: {} created under categoryId :{} successfully",subCategory.getSubCategoryName(),categoryId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("New Subcategory got created successfully in %s",category.getCategoryName()));
    }



    @Override
    public ResponseEntity<String> deleteSubCategoryById(Long subCategoryId) {
        LOGGER.info("Received request to delete subcategory with ID: {}", subCategoryId);
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).
                orElseThrow(()->  {
                    LOGGER.error("Subcategory with ID {} not found", subCategoryId);
                    return new ResourceNotFoundException("Subcategory", subCategoryId);
                });

        Category category = subCategory.getCategory();
        LOGGER.info("Subcategory found, removing from category '{}'", category.getCategoryName());

        category.getSubCategories().remove(subCategory);
        subCategoryRepository.deleteById(subCategoryId);
        LOGGER.info("Subcategory with ID {} successfully deleted", subCategoryId);

        return ResponseEntity.status(HttpStatus.OK).body("SubCategory got deleted successfully");
    }

    @Override
    public ResponseEntity<String> deleteSubCategoryByCategory(Category category) {
        LOGGER.info("Received request to delete all subcategories under category with ID: {}", category.getCategoryId());
        Category categoryResult = categoryRepository.findByCategoryId(category.getCategoryId())
                .orElseThrow(() -> {
                    LOGGER.error("Category with ID {} not found", category.getCategoryId());
                    return new ResourceNotFoundException("Category");
                });

        categoryResult.getSubCategories().clear();
        LOGGER.info("All subcategories under category '{}' have been cleared", categoryResult.getCategoryName());

        return ResponseEntity.status(HttpStatus.OK).body("SubCategory got deleted successfully");
    }

    @Override
    public ResponseEntity<String> updateSubCategoryName(Long subCategoryId,SubCategoryRequestDTO subCategoryRequestDTO) {
        LOGGER.info("Received request to update subcategory with ID: {}", subCategoryId);
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(()->  {
            LOGGER.error("Subcategory with ID {} not found", subCategoryId);
            return new ResourceNotFoundException("Subcategory", subCategoryId);
        });

        SubCategory subCategoryMapped = modelMapper.map(subCategoryRequestDTO,SubCategory.class);

        boolean isDuplicateCategoryExist = subCategoryRepository.findAll().stream()
                .anyMatch(c -> c.getSubCategoryName().equals(subCategoryMapped.getSubCategoryName()));

        if (isDuplicateCategoryExist) {
            LOGGER.warn("Duplicate subcategory name '{}' found for category. Update failed.", subCategoryMapped.getSubCategoryName());
            throw new DuplicateEntryException("SubCategory", subCategoryMapped.getSubCategoryName());
        }

        subCategory.setSubCategoryName(subCategoryRequestDTO.getSubCategoryName());
        subCategoryRepository.save(subCategory);
        LOGGER.info("Subcategory with ID {} updated successfully to name '{}'", subCategoryId, subCategoryRequestDTO.getSubCategoryName());

        return ResponseEntity.status(HttpStatus.OK).body("SubCategory updated successfully ");
    }

    @Override
    public ResponseEntity<SubCategoryResponseDTO> getAllSubCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        LOGGER.info("Received request to get subcategories. Page number: {}, Page size: {}, Sort by: {}, Sort order: {}", pageNumber, pageSize, sortBy, sortOrder);

        Sort sortByOrder;
        if(sortOrder.equals("default")){
            sortByOrder = Sort.unsorted();
            LOGGER.info("Received request to get subcategories. Page number: {}, Page size: {}, Sort by: {}, Sort order: {}", pageNumber, pageSize, sortBy, sortOrder);

        }else {
            sortByOrder = sortOrder.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
            LOGGER.info("Using {} sort order for '{}'", sortOrder, sortBy);
        }
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByOrder);
        Page<SubCategory> pageResult = subCategoryRepository.findAll(pageDetails);

        List<SubCategory> subCategory = pageResult.getContent();
        if(subCategory.isEmpty()){
                LOGGER.warn("No subcategories found for the requested page.");
                throw new ResourceNotFoundException("Subcategories");
        }

        List<SubCategoryRequestDTO> subCategoryRequestDTOList = subCategory.stream()
                .map(subCategories -> modelMapper.map(subCategories,SubCategoryRequestDTO.class)).toList();
        subCategoryResponseDTO.setSubCategoryRequestDTOList(subCategoryRequestDTOList);
        subCategoryResponseDTO.setPageNumber(pageResult.getNumber());
        subCategoryResponseDTO.setPageSize(pageResult.getSize());
        subCategoryResponseDTO.setTotalElements(pageResult.getTotalElements());
        subCategoryResponseDTO.setTotalPages(pageResult.getTotalPages());
        subCategoryResponseDTO.setLastPage(pageResult.isLast());


        LOGGER.info("Returning subcategory data. Total subcategories: {}, Total pages: {}", pageResult.getTotalElements(), pageResult.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(subCategoryResponseDTO);
    }

    @Override
    public ResponseEntity<SubCategoryUserResponseDTO> getSubCategoriesByCategory(Long categoryId) {
        LOGGER.info("Received request to get subcategories for category ID: {}", categoryId);

        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() ->  {
                    LOGGER.error("Category with ID {} not found", categoryId);
                    return new ResourceNotFoundException("Category", categoryId);
                });
        List<SubCategory> subCategoriesList = subCategoryRepository.findByCategoryOrderBySubCategoryNameAsc(category);
        if(subCategoriesList.isEmpty()){
            LOGGER.warn("No subcategories found for category with ID: {}", categoryId);
            throw new ResourceNotFoundException("Subcategories");
        }
        List<SubCategoryRequestDTO> categoryResponseDTOList = subCategoriesList.stream()
                .map(subCategory -> modelMapper.map(subCategory,SubCategoryRequestDTO.class)).collect(Collectors.toList());
        subCategoryUserResponseDTO.setSubCategoryRequestDTOList(categoryResponseDTOList);

        LOGGER.info("Returning {} subcategories for category ID: {}", subCategoriesList.size(), categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(subCategoryUserResponseDTO);
    }


    public ResponseEntity<String> getSubCategoriesByKeyword(){
        return null;
    }
}
