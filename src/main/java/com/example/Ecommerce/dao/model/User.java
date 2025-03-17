package com.example.Ecommerce.dao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users",uniqueConstraints = {
        @UniqueConstraint(columnNames = "email_address")
})
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "user_id")
    private Long userId;


    @NotBlank
    @Column(name ="user_name")
    private String userName;

    @NotBlank
    @Column(name ="password")
    private String password;

    @NotBlank
    @Column(name = "email_address")
    @Email(message = "Enter a valid email id only numbers and alphabets allowed. sample format xxx123@gmail.com")
    private String emailAddress;

    public boolean isAccountNonExpired = true;


    public boolean isAccountNonLocked = true;

    public boolean isCredentialsNonExpired = true;

    public boolean isEnabled = true;




    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<UserRole> userRoles =  new HashSet<>();


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Product> product = new HashSet<>();



    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name = "user_address",
                        joinColumns = @JoinColumn(name = "user_id"),
                        inverseJoinColumns = @JoinColumn(name = "address_id")
                )
    private Set<Address> address = new HashSet<>();






    public  void addProducts(Product product){
        this.product.add(product);
    }

    public void addUserRoles(UserRole userRole) {
        this.userRoles.add(userRole);
    }


    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

}
