package com.example.Ecommerce.config;

import com.example.Ecommerce.service.security.CustomUserDetailsService;
import com.example.Ecommerce.service.security.JwtAuthenticationFilter;
import com.example.Ecommerce.service.security.JwtUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

   private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
     CustomUserDetailsService customUserDetailsService;


    @Autowired
     JwtAuthenticationFilter jwtAuthenticationFilter;




    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        LOGGER.info("Configuring Http Security for application...!!");
        http.authorizeHttpRequests(
                authorizeRequests ->
                        authorizeRequests.requestMatchers("/auth/user/register","/auth/user/login").permitAll()
                                .anyRequest().authenticated()
        );
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(customizer -> customizer.disable());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        LOGGER.info("Security filter chain configured successfully.");
        return http.build();
    }
    @Bean
    AuthenticationProvider authenticationProvider(){
        LOGGER.debug("Setting up custom authentication provider.");
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
       LOGGER.debug("Custom authentication provider configured successfully.");

        return daoAuthenticationProvider;
    };
    @Bean
    UserDetailsService userDetailsService(){
        LOGGER.debug("Setting up user details service.");
        return  customUserDetailsService;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
       LOGGER.info("Instantiating and returning the BcryptPasswordEncoder");
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
       LOGGER.info("getting and returning the AuthenticationManager using AuthenticationConfig");
       return authenticationConfiguration.getAuthenticationManager();
    }





}
