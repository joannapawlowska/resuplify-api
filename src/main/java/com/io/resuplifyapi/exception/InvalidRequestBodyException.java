package com.io.resuplifyapi.exception;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class InvalidRequestBodyException extends RuntimeException{

    String message;

    public InvalidRequestBodyException(Errors errors){
        super();
        setMessage(errors);
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(Errors errors) {
        FieldError e = errors.getFieldError();
        message = String.format("%s %s", e.getField(), e.getDefaultMessage());
    }
}
