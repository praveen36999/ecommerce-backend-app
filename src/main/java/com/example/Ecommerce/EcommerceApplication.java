package com.example.Ecommerce;

import com.example.Ecommerce.controller.CategoryAdminController;
import com.example.Ecommerce.controller.ProductAdminController;
import com.example.Ecommerce.dao.model.User;
import com.example.Ecommerce.service.security.CustomUserDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.Comparator;


@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(EcommerceApplication.class, args);



	}
}
