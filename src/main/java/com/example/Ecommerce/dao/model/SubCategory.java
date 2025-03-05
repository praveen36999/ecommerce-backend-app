package com.example.Ecommerce.dao.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class SubCategory {
    @Id
    @Column(name = "subcategory_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long subCategoryId;

    @Column(name = "subcategory_name")
    private String subCategoryName;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "subCategory")
    private Set<Product> products;

    public void addProduct(Product product){
        product.setSubCategory(this);
        this.products.add(product);
    }

}
