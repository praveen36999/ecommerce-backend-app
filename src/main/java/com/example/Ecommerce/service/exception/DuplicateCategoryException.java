package com.example.Ecommerce.service.exception;

import com.example.Ecommerce.dao.model.Category;

public class DuplicateCategoryException extends RuntimeException{
    String resourceName;
    String fieldName;
    String message;

    public DuplicateCategoryException(String field, String fieldName) {
        super(String.format("%s Name with %s is exists already",field,fieldName,field));
        this.resourceName = field;
        this.fieldName = fieldName;
    }
}
