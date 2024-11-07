package com.ssgc.springbootjwt.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.ssgc.springbootjwt.exception.TokenRefreshException;


/**
 * Global exception handler for handling TokenRefreshException specifically.
 * It provides a custom response structure whenever a TokenRefreshException occurs.
 */
@RestControllerAdvice
public class TokenControllerAdvice {

  /**
   * Handles TokenRefreshException thrown within the application.
   * Responds with an HTTP 403 Forbidden status and a custom error message structure.
   *
   * @param ex The TokenRefreshException that was thrown
   * @param request The web request during which the exception occurred
   * @return An ErrorMessage object containing details about the error
   */
  @ExceptionHandler(value = TokenRefreshException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {

    // Create and return a custom ErrorMessage with a 403 status, the exception message,
    // and request details
	  
    return new ErrorMessage(
        HttpStatus.FORBIDDEN.value(),         // Sets HTTP status code to 403
        new Date(),                           // Sets the timestamp to the current date and time
        ex.getMessage(),                      // Uses the exception's message as the error message
        request.getDescription(false)         // Provides additional details about the request
    );
  }
}