package com.io.resuplifyapi.config.security;

import com.io.resuplifyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");

        if (hasBearerToken(header)) {
            final String jwtToken = header.substring(7);

            if (isTokenValid(jwtToken)) {
                setAuthentication(jwtToken, request);
            }
        }

        chain.doFilter(request, response);
    }

    private boolean hasBearerToken(String header){
        return header != null && header.startsWith("Bearer");
    }

    private boolean isTokenValid(String jwtToken){
        return jwtTokenProvider.isJwtTokenValid(jwtToken);
    }

    private void setAuthentication(String jwtToken, HttpServletRequest request){

        UserDetails userDetails = getUserDetails(jwtToken);

        var authToken = new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), null, null
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private UserDetails getUserDetails(String token){
        String username = jwtTokenProvider.extractUsername(token);
        return userService.findByUsername(username);
    }
}