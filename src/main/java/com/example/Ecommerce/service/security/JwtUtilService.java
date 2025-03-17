package com.example.Ecommerce.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtilService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtilService.class);

    private  String secretKey;

    private static final long jwtExpirationMs = 24*60*60*1000;

    JwtUtilService()
    {
        this.secretKey = generateKey();
    }

    private String generateKey()  {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGenerator.generateKey();
            LOGGER.info("Secret key generated successfully.");
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }catch(Exception e){
            LOGGER.error("Error creating a secret key: {}", e.getMessage());
            return "Error creating a secret key :" +e.getMessage();
        }
    }

    public String generateToken(String emailAddress) {
        Map<String,Object> claims = new HashMap<>();
        return Jwts.builder()
                .claims(claims)  // Only if you need custom claims
                .subject(emailAddress)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getKey())  // Algorithm inferred from key
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
       LOGGER.debug("Returning key from secret key");
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        final String username = extractUsernameFromToken(jwtToken);
        return (username.equals(userDetails.getUsername())) && (!isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }

    private <T>T extractClaim(String jwtToken, Function<Claims,T> claimResolver) {
       final  Claims claims = extractAllClaims(jwtToken);
       return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public String extractUsernameFromToken(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }
}
