package com.example.Ecommerce.dao;

import com.example.Ecommerce.dao.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByCategoryName(String categoryName);


    boolean existsByCategoryName(String categoryName);

    Optional<Category>  findByCategoryId(Long categoryId);


}
