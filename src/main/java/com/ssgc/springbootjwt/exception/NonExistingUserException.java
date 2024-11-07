package com.ssgc.springbootjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NonExistingUserException extends RuntimeException {
    public NonExistingUserException(String message) {
    	super(message);
    }
}