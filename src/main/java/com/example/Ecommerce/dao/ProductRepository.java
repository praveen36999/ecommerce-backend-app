package com.example.Ecommerce.dao;

import com.example.Ecommerce.dao.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    boolean existsByProductName(String productName);

    Page<Product> findByProductNameLikeIgnoreCase(String s, Pageable pageDetails);
}
