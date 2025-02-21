package com.example.Ecommerce;

import com.example.Ecommerce.controller.CategoryAdminController;
import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dto.CategoryResponseDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import java.util.*;


@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(EcommerceApplication.class, args);
		System.out.println("Application Started");







	}
}
