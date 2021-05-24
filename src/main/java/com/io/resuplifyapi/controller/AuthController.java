package com.io.resuplifyapi.controller;

import com.io.resuplifyapi.config.security.JwtTokenProvider;
import com.io.resuplifyapi.domain.SuccessResponse;
import com.io.resuplifyapi.domain.UserDto;
import com.io.resuplifyapi.domain.externalAPI.AuthResponse;
import com.io.resuplifyapi.exception.InvalidCredentialsException;
import com.io.resuplifyapi.exception.InvalidRequestBodyException;
import com.io.resuplifyapi.service.ExternalAPIService;
import com.io.resuplifyapi.service.ShopService;
import com.io.resuplifyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value="/auth")
public class AuthController {

    @Autowired
    private ExternalAPIService externalAPIService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ShopService shopService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping(value = "/signup", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> registerUserAccount(@Valid @RequestBody UserDto userDto, Errors errors) {

        if(errors.hasErrors())
            throw new InvalidRequestBodyException(errors);

        AuthResponse response = externalAPIService.authenticateUserAccount(userDto);
        userService.createNewUserAccount(userDto, response);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Successfully registered"));
    }

    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> authenticateUserAccount(@RequestBody UserDto userDto){

        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));

        }catch(BadCredentialsException e){
            throw new InvalidCredentialsException(e.getMessage());
        }

        shopService.refreshExternalAPITokenIfRequired(userDto);

        final UserDetails userDetails = userService.loadUserByUsername(userDto.getUsername());
        final String jwtToken = jwtTokenProvider.createJwtToken(userDetails);
        final int expirationTime = jwtTokenProvider.getJwtExpirationMs();

        return ResponseEntity.status(HttpStatus.OK).body(new AuthResponse(jwtToken, expirationTime));
    }
}