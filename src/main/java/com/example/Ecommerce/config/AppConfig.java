package com.example.Ecommerce.config;


import com.example.Ecommerce.service.security.CustomUserDetailsService;
import com.example.Ecommerce.service.security.JwtAuthenticationFilter;
import com.example.Ecommerce.service.security.JwtUtilService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    public static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);
    @Bean
    public ModelMapper modelMapper(){
        LOGGER.info("In App config: To instantiate and return model mapper object");
        return new ModelMapper();

    }


}
