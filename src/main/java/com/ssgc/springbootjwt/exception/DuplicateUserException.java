package com.ssgc.springbootjwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class DuplicateUserException extends RuntimeException {

	    public DuplicateUserException(String message) {
	    	super(message);
	    }
	
}
