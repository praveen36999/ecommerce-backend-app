package com.example.Ecommerce.dao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email_address")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;


    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @NotBlank
    @Column(name = "email_address")
    private String emailIAddress;

    @ManyToMany()
    @JoinTable(
            name ="user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> userRoles = new HashSet<>();


    public void addUserRoles(UserRole userRole) {
      userRole.addUsers(this);
      userRoles.add(userRole);
    }
}
