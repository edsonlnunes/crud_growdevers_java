package br.com.growdev.growdevers.dtos;

import org.springframework.validation.FieldError;

public record ErrorData(String field, String message){
    public ErrorData(FieldError error){
        this(error.getField(), error.getDefaultMessage());
    }
}