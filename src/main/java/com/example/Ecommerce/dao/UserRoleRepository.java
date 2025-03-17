package com.example.Ecommerce.dao;

import com.example.Ecommerce.dao.model.User;
import com.example.Ecommerce.dao.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    UserRole findByUser(User user);


}
