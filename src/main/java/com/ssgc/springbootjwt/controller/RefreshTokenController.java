package com.ssgc.springbootjwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssgc.springbootjwt.config.JwtTokenUtil;
import com.ssgc.springbootjwt.exception.TokenRefreshException;
import com.ssgc.springbootjwt.model.RefreshToken;
import com.ssgc.springbootjwt.models.TokenRefreshRequest;
import com.ssgc.springbootjwt.models.TokenRefreshResponse;
import com.ssgc.springbootjwt.service.JwtUserDetailsService;
import com.ssgc.springbootjwt.service.RefreshTokenService;

@RestController
@RequestMapping("/")
public class RefreshTokenController {

    // Utility class for JWT token generation and validation
    @Autowired
    private JwtTokenUtil jwtUtil;

    // Service for managing user details and user-related operations
    @Autowired
    private JwtUserDetailsService userDetailsService;

    // Service for handling refresh tokens, including creation, verification, and expiration
    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * Endpoint to refresh the access token using a provided refresh token.
     *
     * @param request Contains the refresh token submitted by the client
     * @return ResponseEntity with new access and refresh tokens, or error if invalid
     */
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshAccessToken(@RequestBody TokenRefreshRequest request) {

        // Retrieve the provided refresh token from the request
        String requestRefreshToken = request.getRefreshToken();

        // Find and verify the refresh token's validity and expiration
        RefreshToken refreshToken = refreshTokenService.findByToken(requestRefreshToken)
            .map(refreshTokenService::verifyExpiration) // Verify expiration of the token if found
            .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Invalid or expired refresh token")); // Throw exception if token is invalid or expired

        // Load user details based on the username associated with the refresh token
        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUsername());

        // Generate a new access token for the user
        String accessToken = jwtUtil.generateToken(userDetails);

        // Invalidate the old refresh token and create a new one
        refreshTokenService.deleteByUsername(userDetails.getUsername());
        RefreshToken newrefreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        // Return the new access token and refresh token in the response
        return ResponseEntity.ok(new TokenRefreshResponse(accessToken, newrefreshToken.getToken()));
    }
}