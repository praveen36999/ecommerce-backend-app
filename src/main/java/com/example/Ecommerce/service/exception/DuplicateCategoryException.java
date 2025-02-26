package com.example.Ecommerce.service.exception;

import com.example.Ecommerce.dao.model.Category;

public class DuplicateCategoryException extends RuntimeException{
    String reasourceName;
    String fieldName;
    String message;

    public DuplicateCategoryException(String field, String fieldName) {
        super(String.format("%s Name with %s is exists already in %s",field,fieldName,field));
        this.reasourceName = field;
        this.fieldName = fieldName;
    }
}
