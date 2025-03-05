package com.example.Ecommerce.service.impl;

import com.example.Ecommerce.service.api.CategoryService;
import com.example.Ecommerce.dao.CategoryRepository;
import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dto.CategoryRequestDTO;
import com.example.Ecommerce.dto.CategoryResponseAdminDTO;
import com.example.Ecommerce.service.exception.DuplicateCategoryException;
import com.example.Ecommerce.service.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
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
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryResponseAdminDTO categoryResponseAdminDTO;



    //Retrieves all the categories
    @Override
    public ResponseEntity<CategoryResponseAdminDTO> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortOrder){
        Sort sortByOrder;
        if(sortOrder.equals("default")){
             sortByOrder = Sort.unsorted();
        }else {
             sortByOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        }
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByOrder);
        Page<Category> pageResult = categoryRepository.findAll(pageDetails);


        List<Category> categories = pageResult.getContent();
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("Categories");
        }
        List<CategoryRequestDTO> categoryRequestDTOList= categories.stream()
                .map(category -> modelMapper.map(category,CategoryRequestDTO.class)).toList();

        categoryResponseAdminDTO.setCategoryRequestDTOList(categoryRequestDTOList);
        categoryResponseAdminDTO.setPageNumber(pageResult.getNumber());
        categoryResponseAdminDTO.setPageSize(pageResult.getSize());
        categoryResponseAdminDTO.setTotalElements(pageResult.getTotalElements());
        categoryResponseAdminDTO.setTotalPages(pageResult.getTotalPages());
        categoryResponseAdminDTO.setLastPage(pageResult.isLast());

        return ResponseEntity.status(HttpStatus.OK).body(categoryResponseAdminDTO);
    }

    // it checks whether diff category exist with same name else it updates
    @Override
    public ResponseEntity<String> updateCategoryName(Long categoryIdDTO, CategoryRequestDTO categoryRequestDTO) {

            Category category = modelMapper.map(categoryRequestDTO,Category.class);
            //case 1: checks categoryId
            Category fetchCategory = categoryRepository.findById(categoryIdDTO)
                    .orElseThrow(() -> new ResourceNotFoundException("Category", categoryIdDTO));

            //case 2: checks category name exists already
            boolean isDuplicateCategoryExist = categoryRepository.findAll().stream()
                    .anyMatch(c -> c.getCategoryName().equals(category.getCategoryName()));
            if (isDuplicateCategoryExist) {
                throw new DuplicateCategoryException("Category", category.getCategoryName());
            }

            fetchCategory.setCategoryName(category.getCategoryName());
            categoryRepository.save(fetchCategory);

            return ResponseEntity.status(HttpStatus.OK).body("Category updated successfully");
    }

    //it checks already category with a same name exists else it saves
    @Override
    public ResponseEntity<String> createCategory(@Valid CategoryRequestDTO categoryRequestDTO){
            Category category = modelMapper.map(categoryRequestDTO,Category.class);
        try {
            boolean isCategoryExist = categoryRepository.existsByCategoryName(category.getCategoryName());
            if (isCategoryExist) {
                throw new DuplicateCategoryException("Category", category.getCategoryName());
            }

            categoryRepository.save(category);

            return ResponseEntity.status(HttpStatus.CREATED).body("One new Category has been added");
        }catch(DuplicateCategoryException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while Creating new category");
        }
    }


    //it checks if category exists and removes it
    @Override
    public ResponseEntity<String> deleteCategory(String categoryName){
        try{
        Category category = categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new ResourceNotFoundException("category", categoryName));

        categoryRepository.delete(category);
        return ResponseEntity.status(HttpStatus.OK).body("Category got deleted successfully");
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting category");
        }
    }


    //it checks if category exists and removes it
    @Override
    public ResponseEntity<String>  deleteCategory(Long categoryId){
        try{
         Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("category", categoryId));
        categoryRepository.delete(category);
        return ResponseEntity.status(HttpStatus.OK).body("Category got deleted successfully");
        }catch(ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting category");
        }
    }



}

