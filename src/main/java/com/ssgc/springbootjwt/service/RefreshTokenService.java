package com.ssgc.springbootjwt.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssgc.springbootjwt.dao.RefreshTokenRepository;
import com.ssgc.springbootjwt.dao.UserDao;
import com.ssgc.springbootjwt.exception.TokenRefreshException;
import com.ssgc.springbootjwt.model.BANKUSER;
import com.ssgc.springbootjwt.model.RefreshToken;

@Service 
public class RefreshTokenService {

    // Duration for which the refresh token is valid (3 days)
    private Long refreshTokenDurationMs = 3 * 24 * 60 * 60 * 1000L;
    
    // Injecting the RefreshToken repository for database operations
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    // Injecting the User DAO to fetch user-related data
    @Autowired
    private UserDao userRepository;

    // Method to find a refresh token by its string representation
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    // Method to create a new refresh token for a given username
    public RefreshToken createRefreshToken(String username) {
        // Fetch the user associated with the provided username
        BANKUSER user = userRepository.findByUsername(username);
        
        // Throw an exception if the user does not exist
        if (user == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        // Create a new RefreshToken object
        RefreshToken refreshToken = new RefreshToken();
        
        // Set the username for the refresh token
        refreshToken.setUsername(user.getUsername());
        
        // Set the expiration date based on the current time plus the defined duration
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + refreshTokenDurationMs));
        
        // Generate a unique token string using UUID
        refreshToken.setToken(UUID.randomUUID().toString());

        // Save the refresh token to the database and return it
        return refreshTokenRepository.save(refreshToken);
    }
    
    // Method to verify if the given refresh token has expired
    public RefreshToken verifyExpiration(RefreshToken token) {
        // Compare the expiry date of the token with the current date
        if (token.getExpiryDate().compareTo(new Date(System.currentTimeMillis())) < 0) {
            // If expired, delete the token from the repository
            refreshTokenRepository.delete(token);
            // Throw a TokenRefreshException indicating the token is no longer valid
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }

        // Return the valid token if not expired
        return token;
    }
    
    // Method to delete all refresh tokens associated with a specific username
    @Transactional // Ensures that this operation is executed within a transaction
    public void deleteByUsername(String username) {
        // Find the user by username and delete all associated refresh tokens
        refreshTokenRepository.deleteByUsername(userRepository.findByUsername(username).getUsername());
    }
}
