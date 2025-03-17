package com.example.Ecommerce.dao;

import com.example.Ecommerce.dao.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUserName(String userName);


    User findByEmailAddress(String emailAddress);

    Optional<User> findById(Long userId);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);

    boolean existsByEmailAddress(String emailAddress);
}
