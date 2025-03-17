package com.example.Ecommerce.service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
 public class JwtAuthenticationFilter extends OncePerRequestFilter {



    @Autowired
     JwtUtilService jwtUtilService;

     @Autowired
     CustomUserDetailsService customUserDetailsService;

   private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String username = null;

        if(authHeader!= null && authHeader.startsWith("Bearer ")){
            jwtToken = authHeader.substring(7);
            try {
                username = jwtUtilService.extractUsernameFromToken(jwtToken);
            }catch(Exception e){
              //  LOGGER.error("Error extracting username from token", e);
            }
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() ==  null){
            try {
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
               // LOGGER.info("Attempting to authenticate user: {}", username);

                if (jwtUtilService.validateToken(jwtToken, userDetails)) {

                 //   LOGGER.info("JWT token is valid for user: {}", username);

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                }
                else {
                   // LOGGER.warn("Invalid JWT token for user: {}", username);
                }
            }catch (Exception e) {
               // LOGGER.error("Authentication failed for user: {}", username, e);
            }
        }
        filterChain.doFilter(request,response);
    }
}
