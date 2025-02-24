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

    private String productName;

    private long price;

    private long discountedPrice;

    private String description;

    @ManyToOne
    private SubCategory subCategory;

}
