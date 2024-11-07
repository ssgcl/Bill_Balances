package com.ssgc.springbootjwt.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ssgc.springbootjwt.service.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;

/**
 * JwtRequestFilter is a filter that intercepts each incoming HTTP request, checks for a JWT token in the header, 
 * validates the token, and authenticates the user if the token is valid.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/**
     * This method filters each incoming request. It extracts the JWT token from the Authorization header, 
     * validates the token, and sets up authentication in the Spring Security context if the token is valid.
     *
     * @param request The HttpServletRequest object that represents the request
     * @param response The HttpServletResponse object that represents the response
     * @param chain The filter chain to pass the request and response to the next filter
     * @throws ServletException if a servlet error occurs
     * @throws IOException if an I/O error occurs
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		// Retrieve the Authorization header from the request
		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;
		
		// Check if the token is present and starts with "Bearer "
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			// Extract the token by removing the "Bearer " prefix
			jwtToken = requestTokenHeader.substring(7);
			try {
				// Retrieve username from token
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				System.out.println("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token has expired");
				logger.info("Invalid token or token has expired");
			}
		} else {
			logger.warn("JWT Token does not begin with Bearer String");
		}

		// If a valid username is extracted and no authentication is present in the context, validate the token
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// Load user details from the database
			UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

			// Validate the token with user details to ensure it is authentic
			if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

				// Create an authentication token and set user details and roles
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
				        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				// Set authentication details from the request
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// Set the authenticated user in the SecurityContext
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		// Continue the filter chain
		chain.doFilter(request, response);
	}	

}