package com.example.Ecommerce.service.impl;

import com.example.Ecommerce.config.SecurityConfig;
import com.example.Ecommerce.dao.SubCategoryRepository;
import com.example.Ecommerce.service.api.CategoryService;
import com.example.Ecommerce.dao.CategoryRepository;
import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dto.CategoryRequestDTO;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import com.example.Ecommerce.service.api.SubCategoryService;
import com.example.Ecommerce.service.exception.DuplicateEntryException;
import com.example.Ecommerce.service.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
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
public class CategoryServiceImpl implements CategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryResponseDTO categoryResponseDTO;

    @Autowired
    private SubCategoryService subCategoryService;



    //Retrieves all the categories
    @Override
    public ResponseEntity<CategoryResponseDTO> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder){
        Sort sortByOrder;
        if(sortOrder.equals("default")){
             sortByOrder = Sort.unsorted();
             LOGGER.debug("Sorting order is default. No sorting applied..!!");
        }else {
             sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                     : Sort.by(sortBy).descending();
            LOGGER.debug("Sorting categories by {} in {} order",sortBy,sortOrder);
        }
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByOrder);
        LOGGER.debug("Pageable details: pageNumber={}, pageSize={}, sortByOrder={}",pageNumber,pageSize,sortByOrder);
        Page<Category> pageResult = categoryRepository.findAll(pageDetails);
        LOGGER.info("Fetched {} categories from the category repository",pageResult.getTotalElements());


        List<Category> categories = pageResult.getContent();
        if(categories.isEmpty()){
            LOGGER.warn("No categories found throwing ResourceNotFoundException.");
            throw new ResourceNotFoundException("Categories");
        }
        LOGGER.debug("Mapping categories to DTO objects...");
        List<CategoryRequestDTO> categoryRequestDTOList= categories.stream()
                .map(category -> modelMapper.map(category,CategoryRequestDTO.class)).toList();

        categoryResponseDTO.setCategoryRequestDTOList(categoryRequestDTOList);
        categoryResponseDTO.setPageNumber(pageResult.getNumber());
        categoryResponseDTO.setPageSize(pageResult.getSize());
        categoryResponseDTO.setTotalElements(pageResult.getTotalElements());
        categoryResponseDTO.setTotalPages(pageResult.getTotalPages());
        categoryResponseDTO.setLastPage(pageResult.isLast());

        LOGGER.info("Returning response with {} categories, page {} of {}",
                categoryRequestDTOList.size(), pageResult.getNumber(), pageResult.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseDTO);
    }

    // it checks whether diff category exist with same name else it updates
    @Override
    public ResponseEntity<String> updateCategoryName(Long categoryIdDTO, CategoryRequestDTO categoryRequestDTO) {

        LOGGER.info("Received request to update category name for categoryId: {}",categoryIdDTO);
            Category category = modelMapper.map(categoryRequestDTO,Category.class);
        LOGGER.debug("Mapped CategoryRequestDTO to Category entity: {}", category);

            //case 1: checks categoryId
            Category fetchCategory = categoryRepository.findById(categoryIdDTO)
                    .orElseThrow(() ->{
                        LOGGER.error("Category with ID {} not found", categoryIdDTO);
                       return new ResourceNotFoundException("Category", categoryIdDTO);
                    });
        LOGGER.info("Found category with ID: {}", categoryIdDTO);

            //case 2: checks category name exists already
            boolean isDuplicateCategoryExist = categoryRepository.findAll().stream()
                    .anyMatch(c -> c.getCategoryName().equals(category.getCategoryName()));
            if (isDuplicateCategoryExist) {
                LOGGER.warn("Duplicate category name detected: {}", category.getCategoryName());
                throw new DuplicateEntryException("Category", category.getCategoryName());
            }
        LOGGER.info("No duplicate category name found for: {}", category.getCategoryName());

            fetchCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(fetchCategory);
        LOGGER.info("Successfully updated category name to: {}", category.getCategoryName());

            return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
    }

    //it checks already category with a same name exists else it saves
    @Override
    public ResponseEntity<String> createCategory(@Valid CategoryRequestDTO categoryRequestDTO){
        LOGGER.info("Received request to create a new category: {}", categoryRequestDTO.getCategoryName());

            Category category = modelMapper.map(categoryRequestDTO,Category.class);

        try {
            boolean isCategoryExist = categoryRepository.existsByCategoryName(category.getCategoryName());
            if (isCategoryExist) {
                LOGGER.warn("Category with name '{}' already exists.", category.getCategoryName());
                throw new DuplicateEntryException("Category", category.getCategoryName());
            }

            categoryRepository.save(category);
            LOGGER.info("Category with name '{}' has been created successfully.", category.getCategoryName());

            return ResponseEntity.status(HttpStatus.CREATED).body("One new Category has been added");
        }catch(DuplicateEntryException e){
            LOGGER.error("Category creation failed. Duplicate category name: {}", category.getCategoryName(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            LOGGER.error("An error occurred while creating the category: {}", category.getCategoryName(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while Creating new category");
        }
    }


    //it checks if category exists and removes it
    @Override
    public ResponseEntity<String> deleteCategory(String categoryName){
        LOGGER.info("Received request to delete category: {}", categoryName);
        try{
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("category", categoryName));

            LOGGER.info("Category with name '{}' found. Proceeding with deletion.", categoryName);

        categoryRepository.delete(category);
            LOGGER.info("Category with name '{}' deleted successfully.", categoryName);

        return ResponseEntity.status(HttpStatus.OK).body("Category got deleted successfully");
        }catch(ResourceNotFoundException e){
            LOGGER.error("Category with name '{}' not found.", categoryName, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            LOGGER.error("An error occurred while deleting category with name '{}'.", categoryName, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting category");
        }
    }


    //it checks if category exists and removes it
    @Override
    public ResponseEntity<String>  deleteCategory(Long categoryId){
        LOGGER.info("Received request to delete category with ID: {}", categoryId);
        try{
         Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", categoryId));

            LOGGER.info("Category with ID '{}' found. Proceeding with deletion.", categoryId);

            categoryRepository.delete(category);
            LOGGER.info("Category with ID '{}' deleted successfully.", categoryId);

        return ResponseEntity.status(HttpStatus.OK).body("Category got deleted successfully");
        }catch(ResourceNotFoundException e){
            LOGGER.error("Category with ID '{}' not found.", categoryId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            LOGGER.error("An error occurred while deleting category with ID '{}'.", categoryId, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting category");
        }
    }



}

