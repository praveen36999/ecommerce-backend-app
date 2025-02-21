package com.example.Ecommerce.category;


import com.example.Ecommerce.dao.CategoryRepository;
import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dto.CategoryRequestDTO;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import com.example.Ecommerce.service.api.CategoryService;
import com.example.Ecommerce.service.exception.ResourceNotFoundException;
import com.example.Ecommerce.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.Optional;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private Category category;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRequestDTO categoryRequestDTO;
    @Mock
    private CategoryResponseDTO categoryResponseDTO;
    @Mock
    private ResponseEntity<String> response;
    @Mock
    private ResponseEntity<CategoryResponseDTO> categoryResponseDTOResponseEntity;

    @BeforeEach
    void validRequestSetup(){
        categoryRequestDTO = new CategoryRequestDTO();
        category = new Category();
    }

    @Test
    void createCategory_WithValid_CategoryName() {
        categoryRequestDTO.setCategoryId(1L);
        categoryRequestDTO.setCategoryName("Electronics");
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        category.setCategoryId(categoryRequestDTO.getCategoryId());
        Mockito.when(modelMapper.map(categoryRequestDTO, Category.class)).thenReturn(category);
        Mockito.when(categoryRepository.existsByCategoryName(category.getCategoryName()))
                .thenReturn(false);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);

        response = categoryService.createCategory(categoryRequestDTO);

        Mockito.verify(categoryRepository, times(1)).save(category);
        Assertions.assertEquals(HttpStatus.CREATED,response.getStatusCode());
        Assertions.assertEquals("One new Category has been added",response.getBody());
    }

    @Test
    void createCategory_WithInvalid_CategoryName(){
        categoryRequestDTO.setCategoryId(1L);
        categoryRequestDTO.setCategoryName("Electronics");
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        category.setCategoryId(categoryRequestDTO.getCategoryId());

        Mockito.when(modelMapper.map(categoryRequestDTO, Category.class)).thenReturn(category);
        Mockito.when(categoryRepository.existsByCategoryName(category.getCategoryName()))
                .thenReturn(true);
        response = categoryService.createCategory(categoryRequestDTO);

        Mockito.verify(categoryRepository, times(0)).save(category);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
        Assertions.assertEquals("Category Name with Electronics is already exists in Category",response.getBody());
    }

    @Test
    void createCategory_WithUnexpectedRuntimeError(){
        categoryRequestDTO.setCategoryId(1L);
        categoryRequestDTO.setCategoryName("Electronics");
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        category.setCategoryId(categoryRequestDTO.getCategoryId());

        Mockito.when(modelMapper.map(categoryRequestDTO, Category.class)).thenReturn(category);
        Mockito.when(categoryRepository.existsByCategoryName(category.getCategoryName()))
                .thenReturn(false);
        Mockito.when(categoryRepository.save(category)).thenThrow(new RuntimeException("An error occurred while Creating new category"));
        response = categoryService.createCategory(categoryRequestDTO);

        Mockito.verify(categoryRepository, times(1)).save(category);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        Assertions.assertEquals("An error occurred while Creating new category",response.getBody());
    }

    @Test
    void deleteCategory_WithValid_CategoryName() {
        categoryRequestDTO.setCategoryName("Electronics");
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        Optional<Category> optionalCategory = Optional.of(category);
        Mockito.when(categoryRepository.findByCategoryName(category.getCategoryName()))
                .thenReturn(optionalCategory);
        Mockito.doNothing().when(categoryRepository).delete(category);
        response = categoryService.deleteCategory(category.getCategoryName());
        Mockito.verify(categoryRepository, times(1)).delete(category);
        Assertions.assertEquals(HttpStatus.OK,response.getStatusCode());
        Assertions.assertEquals("Category got deleted successfully",response.getBody());
    }

    @Test
    void deleteCategory_WithInvalid_CategoryName() {
        categoryRequestDTO.setCategoryName("xxxx");
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        Optional<Category> optionalCategory = Optional.of(category);
        Mockito.when(categoryRepository.findByCategoryName(category.getCategoryName()))
                .thenThrow(new ResourceNotFoundException("category", category.getCategoryName()));
        response = categoryService.deleteCategory(category.getCategoryName());
        Mockito.verify(categoryRepository, times(0)).delete(category);
        Assertions.assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        Assertions.assertEquals("xxxx doesn't exist in category.Enter a valid category name",response.getBody());
    }

    @Test
    void deleteCategory_WithUnexpectedRuntimeError() {
        categoryRequestDTO.setCategoryName("Electronics");
        category.setCategoryName(categoryRequestDTO.getCategoryName());
        Optional<Category> optionalCategory = Optional.of(category);
        Mockito.when(categoryRepository.findByCategoryName(category.getCategoryName()))
                .thenReturn(optionalCategory);
        Mockito.doThrow(new RuntimeException("An error occurred while deleting category")).when(categoryRepository).delete(category);
        response = categoryService.deleteCategory(category.getCategoryName());
        Mockito.verify(categoryRepository, times(1)).delete(category);
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        Assertions.assertEquals("An error occurred while deleting category",response.getBody());
    }

    @Test
    void GetAllCategories_InAscendingOrder(){
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "categoryName";
        String sortOrder = "asc";
        List<Category> categories = List.of(
                new Category(1L, "Books"),
                new Category(2L, "Clothing"),
                new Category(3L, "Electronics"));

        Page<Category> pageResult = new PageImpl<>(categories);
         Mockito.when(categoryRepository.findAll(any(Pageable.class)))
                        .thenReturn(pageResult);

         List<Category> categoryPage = pageResult.getContent().stream().toList();

         List<CategoryRequestDTO> categoryRequestDTOList = categoryPage.stream()
                         .map(category1 -> modelMapper.map(category1,CategoryRequestDTO.class)).toList();

        categoryResponseDTO.setCategoryRequestDTOList(categoryRequestDTOList);
        categoryResponseDTO.setPageNumber(pageNumber);
        categoryResponseDTO.setPageSize(pageSize);
        categoryResponseDTO.setTotalPages(pageResult.getTotalPages());
        categoryResponseDTO.setTotalElements(pageResult.getSize());
        categoryResponseDTO.setLastPage(pageResult.isLast());

        categoryResponseDTOResponseEntity = categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder);

        Mockito.verify(categoryRepository,times(1)).findAll(any(Pageable.class));
        Assertions.assertEquals(0,categoryResponseDTOResponseEntity.getBody().getPageNumber());
        Assertions.assertEquals(true,categoryResponseDTOResponseEntity.getBody().isLastPage());
    }

}
