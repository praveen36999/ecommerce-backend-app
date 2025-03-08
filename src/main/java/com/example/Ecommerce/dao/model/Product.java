package com.example.Ecommerce.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int productId;



    private String productName;

    private double price;

    private int quantity;

    private String description;

    private String image;

    private boolean isAvailable;

    private double discount;

    private double discountedPrice;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    @JsonIgnore
    private SubCategory subCategory;


    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }


}
