package com.example.Ecommerce.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class EcommerceGlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<Map<String,String>> myMethodArgumentNoValidException(MethodArgumentNotValidException e){
        Map<String,String> response = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(error ->{
                    String fieldName = ((FieldError)error).getField();
                    String message = error.getDefaultMessage();
                    response.put(fieldName,message);
                });
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateEntryException.class)
    public ResponseEntity<String> duplicateCategoryException(DuplicateEntryException e){
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> resourceNotFoundException(ResourceNotFoundException e){
        String message = e.getMessage();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

}
