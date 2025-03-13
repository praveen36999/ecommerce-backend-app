package com.example.Ecommerce.dao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles",uniqueConstraints = {
        @UniqueConstraint(columnNames = "role_name")
})
public class UserRole {


   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private long roleId;


   private String roleName;



   @ManyToMany(mappedBy = "userRoles")
   Set<User> users = new HashSet<>();

   public void addUsers(User user) {
      users.add(user);
   }
}
