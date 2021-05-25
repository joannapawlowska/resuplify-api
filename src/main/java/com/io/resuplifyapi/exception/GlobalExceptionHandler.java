package com.io.resuplifyapi.exception;

import com.io.resuplifyapi.domain.ApiError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

    private ResponseEntity<Object> buildResponse(ApiError error){
        return new ResponseEntity<>(error, error.getStatus());
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    protected ResponseEntity<Object> handleUserAlreadyExistException(UserAlreadyExistException e){
        return buildResponse(new ApiError(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(ExternalAPIAuthException.class)
    protected ResponseEntity<Object> handleExternalAPIAuthException(ExternalAPIAuthException e){
        return buildResponse(new ApiError(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(ExternalAPIUnavailableException.class)
    protected ResponseEntity<Object> handleExternalAPIUnavailableException(ExternalAPIUnavailableException e){
        return buildResponse(new ApiError(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e){
        return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException e){
        return buildResponse(new ApiError(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception e){
        return buildResponse(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request){

        List<String> errors = new ArrayList<>();

        for(FieldError err: e.getBindingResult().getFieldErrors()){
            errors.add(err.getField() + ": " + err.getDefaultMessage());
        }

        return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Validation error", errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        List<String> errors = new ArrayList<>();

        if(e.getRequiredType() == LocalDate.class)
            errors.add(e.getName() + " should be of format 'yyyy-MM-dd'");
        else
            errors.add(e.getName() + " should be of type " + e.getRequiredType().getName());

        return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, "Argument mismatch", errors));
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        String message = "No mapping for " + e.getHttpMethod() + " " + e.getRequestURL();
        return buildResponse(new ApiError(HttpStatus.NOT_FOUND, message));
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        String message = e.getParameterName() + " parameter is missing";
        return buildResponse(new ApiError(HttpStatus.BAD_REQUEST, message));
    }
}