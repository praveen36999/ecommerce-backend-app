package com.example.Ecommerce.service.exception;

import com.example.Ecommerce.dao.model.Category;

public class DuplicateEntryException extends RuntimeException{
    String resourceName;
    String fieldName;
    String message;

    public DuplicateEntryException(String field, String fieldName) {
        super(String.format("%s with %s is exists already",field,fieldName,field));
        this.resourceName = field;
        this.fieldName = fieldName;
    }
}
