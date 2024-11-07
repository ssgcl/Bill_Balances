package com.ssgc.springbootjwt.config;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	//public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	public static final long JWT_TOKEN_VALIDITY =  60 * 60 * 1000L;

	private String secret = secretKey().toString();

	//retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	//retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
    //for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		//return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		
		// Convert the secret string into a Key object using Keys.hmacShaKeyFor()
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // Parse the JWT token using the updated method
        return Jwts.parserBuilder()                // Use parserBuilder instead of parser
                .setSigningKey(key)               // Use a Key object instead of a string
                .build()                          // Build the parser
                .parseClaimsJws(token)            // Parse the claims
                .getBody();                       // Extract the claims from the token
	}

	//check if the token has expired
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	//generate token for user
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername());
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string 
	
	private String doGenerateToken(Map<String, Object> claims, String subject) {
	    Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
	    return Jwts.builder()
	        .setClaims(claims)
	        .setSubject(subject)
	        .setIssuedAt(new Date(System.currentTimeMillis()))
	        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
	        .signWith(key)
	        .compact();
	}

	//validate token
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
       
    // Method to extract username from JWT token
    public String extractUsername(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    

    public SecretKeySpec secretKey() {
        String secretKey = System.getenv("JWT");

        if (secretKey == null) {
            throw new IllegalStateException("JWT environment variable is not set.");
        }

        // Ensure the key is in the correct format for HMAC SHA-512 (Base64 encoded and correct length)
        byte[] decodedKey = Base64.getDecoder().decode(secretKey);
        if (decodedKey.length < 64) {
            throw new IllegalArgumentException("JWT secret key must be at least 64 bytes for HS512.");
        }

        // Convert the byte array to a Key object
        return new SecretKeySpec(decodedKey, SignatureAlgorithm.HS512.getJcaName());
    }

}
