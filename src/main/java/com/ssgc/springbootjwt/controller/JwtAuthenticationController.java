package com.ssgc.springbootjwt.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ssgc.springbootjwt.config.JwtTokenUtil;
import com.ssgc.springbootjwt.dao.RefreshTokenRepository;
import com.ssgc.springbootjwt.dao.UserUpdateDto;
import com.ssgc.springbootjwt.model.BANKUSER;
import com.ssgc.springbootjwt.model.RefreshToken;
import com.ssgc.springbootjwt.model.UserDTO;
import com.ssgc.springbootjwt.models.JwtRequest;
import com.ssgc.springbootjwt.models.JwtResponse;
import com.ssgc.springbootjwt.models.MessageResponse;
import com.ssgc.springbootjwt.service.JwtUserDetailsService;
import com.ssgc.springbootjwt.service.RefreshTokenService;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

	 // Authentication manager to validate user login credentials
	@Autowired
	private AuthenticationManager authenticationManager;

	 // Utility for generating and validating JWT access tokens
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	  // Service for handling user-related operations, including saving and updating users
	@Autowired
	private JwtUserDetailsService userDetailsService;
	
	// Service for managing refresh tokens, including creating and deleting them
	@Autowired
	RefreshTokenService refreshTokenService;
	
	// Repository to handle refresh token persistence
	@Autowired
	RefreshTokenRepository refreshTokenRepository;

	/**
     * Authenticates the user and generates a JWT access token and refresh token.
     *
     * @param authenticationRequest The user's login credentials
     * @return ResponseEntity containing the access token and refresh token
     * @throws Exception if authentication fails
     */
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		
		// Authenticate the user using username and password
//		Authentication authentication = authenticationManager.authenticate(
//		        new UsernamePasswordAuthenticationToken(
//		            authenticationRequest.getUsername(), 
//		            authenticationRequest.getPassword()
//		        )
//		);
		
		// Authenticate user using the JwtUserDetailsService
		userDetailsService.authenticateUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		
		// Set the authentication context in the SecurityContextHolder
		//SecurityContextHolder.getContext().setAuthentication(authentication);
		
		// Load user details for token generation
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		
		// Invalidate any existing refresh tokens for this user to ensure a single session
		refreshTokenService.deleteByUsername(authenticationRequest.getUsername());
		
		// Generate a new access token for the authenticated user
		final String accessToken = jwtTokenUtil.generateToken(userDetails);

		// Create a new refresh token for the user
		RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
			 
		// Return access and refresh tokens in the response
		return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken.getToken()));		
	}

	 /**
     * Registers a new user in the system.
     *
     * @param user The user details to register
     * @return ResponseEntity containing the saved user information
     * @throws Exception if an error occurs during registration
     */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		// Save the new user details and return a response with the saved user information
		return ResponseEntity.ok(userDetailsService.save(user));
	}
	
	 /**
     * Updates the authenticated user's information.
     *
     * @param userUpdateDto The updated user information
     * @param principal The authenticated user's principal
     * @return ResponseEntity containing the updated user information
     */
	@PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateDto userUpdateDto, Principal principal) {
      
		// Update the user information based on the provided username and update data
        BANKUSER updatedUser = userDetailsService.updateUser(userUpdateDto.getUsername(), userUpdateDto);
        
        // Return the updated user information
        return ResponseEntity.ok(updatedUser);
    }
	
	 /**
     * Logs out the user by invalidating their refresh token.
     *
     * @return ResponseEntity with a logout success message
     */
	 @PostMapping("/signout")
	  public ResponseEntity<?> logoutUser(@RequestBody UserUpdateDto userUpdateDto, Principal principal) {
	    // Retrieve user details from the authentication context
//	    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		 UserDetails userDetails = userDetailsService.loadUserByUsername(userUpdateDto.getUsername());
	    
	    // Delete the user's refresh token to complete the logout process
	    refreshTokenRepository.deleteByUsername(userDetails.getUsername());
	    
	    // Return a successful logout message
	    return ResponseEntity.ok(new MessageResponse("Log out successful!"));
	  }
}