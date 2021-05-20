package com.io.resuplifyapi.controller;

import com.io.resuplifyapi.domain.ApiError;
import com.io.resuplifyapi.domain.SuccessResponse;
import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.domain.externalAPI.AuthResponse;
import com.io.resuplifyapi.exception.InvalidRequestBodyException;
import com.io.resuplifyapi.service.ExternalAPIService;
import com.io.resuplifyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    ExternalAPIService externalAPIService;

    @Autowired
    UserService userService;

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerUserAccount(@Valid @RequestBody UserDto userDto, Errors errors) {

        if(errors.hasErrors())
            throw new InvalidRequestBodyException(errors);

        AuthResponse response = externalAPIService.authenticateUserAccount(userDto);
        userService.createNewUserAccount(userDto, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Successfully registered"));
    }
}