package com.io.resuplifyapi.config.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    @Value("${jwt.expiration.ms}")
    public int jwtExpirationMs;

    public String createJwtToken(UserDetails userDetails){

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }
    public String extractUsername(String jwtToken){
        return extractClaims(jwtToken).getSubject();
    }

    private Claims extractClaims(String jwtToken){
        return Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public boolean isJwtTokenValid(String jwtToken){

        try {
            extractClaims(jwtToken);
            return true;

        } catch(JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public int getJwtExpirationMs() {
        return jwtExpirationMs;
    }
}