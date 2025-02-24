package com.example.Ecommerce.dao;

import com.example.Ecommerce.dao.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}
