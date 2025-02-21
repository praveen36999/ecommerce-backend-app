package com.example.Ecommerce.config;

import ch.qos.logback.core.model.Model;
import com.example.Ecommerce.dao.model.Category;
import com.example.Ecommerce.dto.CategoryRequestDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

}
