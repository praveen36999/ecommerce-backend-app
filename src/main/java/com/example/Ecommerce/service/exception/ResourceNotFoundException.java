package com.example.Ecommerce.service.exception;

public class ResourceNotFoundException extends RuntimeException{
    String field;
    String fieldName;
    Long fieldId;

    public ResourceNotFoundException(String fieldName){
        super(String.format("%s requested is not found",fieldName));
        this.fieldName = fieldName;
    }
    public ResourceNotFoundException(String field, String fieldName){
        super(String.format("%s doesn't exist in %s.Enter a valid %s name",fieldName,field,field));
        this.field = field;
        this.fieldName = fieldName;
    }
    public ResourceNotFoundException(String field, Long fieldId){
        super(String.format("%d doesn't exist in %s.Enter a valid %s Id",fieldId,field,field));
        this.field = field;
       this.fieldId = fieldId;
    }
}
