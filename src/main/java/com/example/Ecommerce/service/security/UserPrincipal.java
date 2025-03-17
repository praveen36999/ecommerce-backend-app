package com.example.Ecommerce.service.security;

import com.example.Ecommerce.dao.model.User;
import com.example.Ecommerce.dao.UserRepository;
import com.example.Ecommerce.dao.UserRoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPrincipal implements UserDetails {


    private UserRepository userRepository;


    private UserRoleRepository userRoleRepository;

    private User user;

    @Autowired
    UserPrincipal(User user,UserRepository userRepository,UserRoleRepository userRoleRepository){
        this.user = user;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        User user = userRepository.findByEmailAddress(this.user.getEmailAddress());
        List<GrantedAuthority> grantedAuthorities = user.getUserRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                                .collect(Collectors.toList());
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        //return UserDetails.super.isAccountNonExpired();
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
       // return UserDetails.super.isAccountNonLocked();
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
      //  return UserDetails.super.isCredentialsNonExpired();
        return true;
    }

    @Override
    public boolean isEnabled() {
       // return UserDetails.super.isEnabled();
        return true;
    }
}
