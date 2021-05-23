package com.io.resuplifyapi.exception;

import com.io.resuplifyapi.domain.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler{

    private ResponseEntity<Object>  buildResponse(String error, String message, HttpStatus status){
        ApiError apiError = new ApiError(error, message);
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException e){
        return buildResponse("User already exist", e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ExternalAPIAuthException.class)
    protected ResponseEntity<Object> handleExternalAPIAuthException(ExternalAPIAuthException e){
        return buildResponse("Unauthorized client", e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ExternalAPIUnavailableException.class)
    protected ResponseEntity<Object> handleExternalAPIUnavailableException(ExternalAPIAuthException e){
        return buildResponse("Service unavailable", e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(InvalidRequestBodyException.class)
    protected ResponseEntity<Object> handleInvalidRequestBodyException(InvalidRequestBodyException e){
        return buildResponse("Invalid request body", e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundBodyException(UserNotFoundException e){
        return buildResponse("User not found", e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    protected ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException e){
        return buildResponse("Invalid credentials", e.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}