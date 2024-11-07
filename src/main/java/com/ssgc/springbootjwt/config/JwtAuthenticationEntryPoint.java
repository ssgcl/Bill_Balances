package com.ssgc.springbootjwt.config;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JwtAuthenticationEntryPoint is an implementation of the AuthenticationEntryPoint interface.
 * It is invoked whenever an unauthenticated user tries to access a secured endpoint.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

	private static final long serialVersionUID = -7858869558953243875L;

	/**
     * This method is triggered when an unauthenticated user tries to access a secured endpoint.
     * It returns a 401 Unauthorized response with a JSON-formatted error message.
     *
     * @param request The HttpServletRequest object that triggered the exception
     * @param response The HttpServletResponse object to which the error details are written
     * @param authException The exception that caused this method to be invoked
     * @throws IOException if an input or output error is detected when handling the error response
     */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException {

		// Set the response content type to JSON and status to 401 Unauthorized
	    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

	    // Create a response body with error details
	    final Map<String, Object> body = new HashMap<>();
	    body.put("status", HttpServletResponse.SC_UNAUTHORIZED); // Status code 401
	    body.put("error", "Unauthorized");                       // Error type
	    body.put("message", "Invalid token or token is expired"); // Detailed error message
	    body.put("path", request.getServletPath());               // Path where the error occurred

	    // Convert the response body map to JSON format and write it to the response output stream
	    final ObjectMapper mapper = new ObjectMapper();
	    mapper.writeValue(response.getOutputStream(), body);
	}
}