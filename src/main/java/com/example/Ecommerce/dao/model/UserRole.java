package com.example.Ecommerce.dao.model;

import com.example.Ecommerce.config.UserRoles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles",
        uniqueConstraints = @UniqueConstraint(columnNames= "role_name"))
public class UserRole {


   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
   @Column(name = "role_id")
   private long roleId;

   @Getter
   @Setter
   @NotNull(message = "role name cannot be null")
   @Enumerated(EnumType.STRING)
   @Column(name = "role_name")
   private UserRoles roleName;



   @ManyToMany(mappedBy = "userRoles")
   private Set<User> user = new HashSet<>();

   @Override
   public int hashCode() {
      return Objects.hash(roleId);
   }

   public void addUsers(User user) {
      this.user.add(user);
   }
}
