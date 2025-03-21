package com.example.Ecommerce.dao;

import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dao.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory,Long> {
    boolean existsBySubCategoryName(String subCategoryName);

    List<SubCategory> findByCategoryOrderBySubCategoryNameAsc(Category category);

    void deleteAllByCategory(Category category);

    Optional<SubCategory> findBySubCategoryId(Long subCategoryId);
}
