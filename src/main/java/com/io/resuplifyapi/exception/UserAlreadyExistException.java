package com.io.resuplifyapi.exception;

public class UserAlreadyExistException extends RuntimeException{

     public UserAlreadyExistException(String username, String url){
         super(String.format("There is an account with username '%s' and url '%s'", username, url));
     }
}