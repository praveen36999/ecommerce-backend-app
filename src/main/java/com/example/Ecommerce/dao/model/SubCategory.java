package com.example.Ecommerce.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Objects;
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

    public void setCategory(Category category){
        if (this.category != null && this.category.equals(category)) {
            return;
        }
        this.category = category;
    }

    @OneToMany(mappedBy = "subCategory",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    public void addProduct(Product product){
        product.setSubCategory(this);
        this.products.add(product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subCategoryId);
    }

}
