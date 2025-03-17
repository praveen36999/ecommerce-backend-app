package com.example.Ecommerce.dao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Data
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "Category",uniqueConstraints =
            @UniqueConstraint(columnNames = "category_name"))
public class Category {

    @Id
    @Column(name = "category_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long categoryId;

    @Column(name = "category_name")
    private String categoryName;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<SubCategory> subCategories = new HashSet<>();

    public void addSubCategory(SubCategory subCategory){
        subCategory.setCategory(this);  // Set the Category on SubCategory
        this.subCategories.add(subCategory);  // Add the SubCategory to the Set
    }
    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }
}
