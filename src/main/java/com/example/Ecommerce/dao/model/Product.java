package com.example.Ecommerce.dao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int productId;

    @ManyToOne
    @JoinColumn(name = "subcategory_id")
    private SubCategory subCategory;

    private String productName;

    private String description;

    private String image;

    private double price;

    private int quantity;

    private boolean isAvailable;

    private double discount;

    private double discountedPrice;





}
