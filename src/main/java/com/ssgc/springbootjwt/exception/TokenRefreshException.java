package com.ssgc.springbootjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception class for handling token refresh errors.
 * This exception is thrown when a token refresh operation fails,
 * such as when the refresh token is invalid or expired.
 */
@ResponseStatus(HttpStatus.FORBIDDEN) // This annotation indicates that the response status should be 403 Forbidden
public class TokenRefreshException extends RuntimeException {
    
    private static final long serialVersionUID = 1L; // Unique identifier for serialization

    /**
     * Constructs a new TokenRefreshException with a detailed message.
     *
     * @param token the refresh token that caused the exception
     * @param message the error message providing additional context
     */
    public TokenRefreshException(String token, String message) {
        // Call the superclass constructor with a formatted message
        super(String.format("Failed for [%s]: %s", token, message));
    }
}
