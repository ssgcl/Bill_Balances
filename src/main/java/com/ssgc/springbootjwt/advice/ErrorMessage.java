package com.ssgc.springbootjwt.advice;

import java.util.Date;

/**
 * Represents an error message that provides details for exceptions
 * occurring within the application, such as status, timestamp, and description.
 */
public class ErrorMessage {

  // HTTP status code of the error
  private int statusCode;

  // Timestamp indicating when the error occurred
  private Date timestamp;

  // Brief message describing the error
  private String message;

  // Detailed description of the error, providing additional context
  private String description;

  /**
   * Constructor for initializing an ErrorMessage object with specified details.
   *
   * @param statusCode The HTTP status code associated with the error
   * @param timestamp The time when the error occurred
   * @param message A brief message describing the error
   * @param description A detailed description providing more context for the error
   */
  public ErrorMessage(int statusCode, Date timestamp, String message, String description) {
	  
	  System.out.println("description: "+description);
	  System.out.println("message: "+message);
	  
    this.statusCode = statusCode;
    this.timestamp = timestamp;
    this.message = message;
//    this.description = description;
  }

  // Getter for HTTP status code
  public int getStatusCode() {
    return statusCode;
  }

  // Getter for timestamp
  public Date getTimestamp() {
    return timestamp;
  }

  // Getter for error message
  public String getMessage() {
    return message;
  }

  // Getter for error description
  public String getDescription() {
    return description;
  }
}