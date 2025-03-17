package com.example.Ecommerce.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Product",uniqueConstraints =
            @UniqueConstraint(columnNames = "product_name"))
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int productId;


    @Column(name = "product_name")
    private String productName;

    @Column(name = "original_price")
    private double price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "isAvailable")
    private boolean isAvailable;

    @Column(name = "discount")
    private double discount;

    @Column(name = "discounted_price")
    private double discountedPrice;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    @JsonIgnore
    private SubCategory subCategory;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    public void  addUser(User user){
        this.user = user;
    }



    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }




}
