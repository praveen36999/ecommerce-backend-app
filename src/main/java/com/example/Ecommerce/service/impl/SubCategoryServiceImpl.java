package com.example.Ecommerce.service.impl;

import com.example.Ecommerce.dao.CategoryRepository;
import com.example.Ecommerce.dao.SubCategoryRepository;
import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dao.model.SubCategory;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import com.example.Ecommerce.dto.SubCategoryRequestDTO;
import com.example.Ecommerce.dto.SubCategoryResponseDTO;
import com.example.Ecommerce.dto.SubCategoryUserResponseDTO;
import com.example.Ecommerce.service.api.SubCategoryService;
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
import java.util.stream.Collectors;

@Service
public class SubCategoryServiceImpl implements SubCategoryService {
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
        //check valid category exist
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category",categoryId ));
        //mapping subcategoryDto to subcategory
        SubCategory subCategory = modelMapper.map(subCategoryRequestDTO,SubCategory.class);

        //check if already subcategory exist under targeted category
        boolean isSubCategoryExists = subCategoryRepository
                .existsBySubCategoryName(subCategory.getSubCategoryName());
        if(isSubCategoryExists){
            throw new DuplicateCategoryException("Subcategory",subCategory.getSubCategoryName());
        }
        try {
            subCategory.setCategory(category);
            subCategoryRepository.save(subCategory);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(String.format("Subcategory got created successfully in %s",category.getCategoryName()));
    }



    @Override
    public ResponseEntity<String> deleteSubCategoryById(Long subCategoryId) {
        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(()->  new ResourceNotFoundException("Subcategory",subCategoryId));
        subCategoryRepository.deleteById(subCategoryId);
        return ResponseEntity.status(HttpStatus.OK).body("SubCategory got deleted successfully");
    }

    @Override
    public ResponseEntity<String> updateSubCategoryName(Long subCategoryId,SubCategoryRequestDTO subCategoryRequestDTO) {

        SubCategory subCategory = subCategoryRepository.findById(subCategoryId).orElseThrow(()->  new ResourceNotFoundException("Subcategory",subCategoryId));

        SubCategory subCategoryMapped = modelMapper.map(subCategoryRequestDTO,SubCategory.class);

        boolean isDuplicateCategoryExist = subCategoryRepository.findAll().stream()
                .anyMatch(c -> c.getSubCategoryName().equals(subCategoryMapped.getSubCategoryName()));
        if (isDuplicateCategoryExist) {
            throw new DuplicateCategoryException("SubCategory", subCategoryMapped.getSubCategoryName());
        }

        subCategory.setSubCategoryName(subCategoryRequestDTO.getSubCategoryName());
        subCategoryRepository.save(subCategory);
        return ResponseEntity.status(HttpStatus.OK).body("SubCategory updated successfully ");
    }

    @Override
    public ResponseEntity<SubCategoryResponseDTO> getAllSubCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortByOrder;
        if(sortOrder.equals("default")){
            sortByOrder = Sort.unsorted();
        }else {
            sortByOrder = sortOrder.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        }
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByOrder);
        Page<SubCategory> pageResult = subCategoryRepository.findAll(pageDetails);

        List<SubCategory> subCategory = pageResult.getContent();
        if(subCategory.isEmpty()){
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

        return ResponseEntity.status(HttpStatus.OK).body(subCategoryResponseDTO);
    }

    @Override
    public ResponseEntity<SubCategoryUserResponseDTO> getSubCategoriesByCategory(Long categoryId) {
        Category category = categoryRepository.findByCategoryId(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category",categoryId ));
        List<SubCategory> subCategoriesList = subCategoryRepository.findByCategoryOrderBySubCategoryNameAsc(category);
        if(subCategoriesList.isEmpty()){
            throw new ResourceNotFoundException("Subcategories");
        }
        List<SubCategoryRequestDTO> categoryResponseDTOList = subCategoriesList.stream()
                .map(subCategory -> modelMapper.map(subCategory,SubCategoryRequestDTO.class)).collect(Collectors.toList());
        subCategoryUserResponseDTO.setSubCategoryRequestDTOList(categoryResponseDTOList);

        return ResponseEntity.status(HttpStatus.OK).body(subCategoryUserResponseDTO);
    }


    public ResponseEntity<String> getSubCategoriesByKeyword(){
        return null;
    }
}
