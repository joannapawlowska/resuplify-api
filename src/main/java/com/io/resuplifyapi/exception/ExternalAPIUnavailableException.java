package com.io.resuplifyapi.exception;

public class ExternalAPIUnavailableException extends RuntimeException{

    public ExternalAPIUnavailableException(String message){
        super(message);
    }
}