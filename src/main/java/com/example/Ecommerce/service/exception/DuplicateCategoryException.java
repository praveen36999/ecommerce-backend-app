package com.example.Ecommerce.service.exception;

import com.example.Ecommerce.dao.model.Category;

public class DuplicateCategoryException extends RuntimeException{
    String reasourcename;
    String fieldName;
    String message;

    public DuplicateCategoryException(String field, String fieldName) {
        super(String.format("Category Name with %s is already exists in %s",fieldName,field));
        this.reasourcename = field;
        this.fieldName = fieldName;
    }
}
