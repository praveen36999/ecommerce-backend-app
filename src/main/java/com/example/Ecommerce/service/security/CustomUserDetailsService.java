package com.example.Ecommerce.service.security;

import com.example.Ecommerce.dao.UserRoleRepository;
import com.example.Ecommerce.dao.model.User;
import com.example.Ecommerce.dao.UserRepository;
import com.example.Ecommerce.service.impl.UserAuthenticationServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.info("Attempting to load user with username (email): {}", username);

        User user = userRepository.findByEmailAddress(username);
        System.out.println(user.getUserName()+ " " +"in loadUser");
        if(user ==null){
            LOGGER.error("User with username (email) {} not found", username);
            throw new UsernameNotFoundException("User not Found");
        }
        LOGGER.info("User {} found in the system", user.getUserName());
        return new UserPrincipal(user,userRepository,userRoleRepository);
    }
}
