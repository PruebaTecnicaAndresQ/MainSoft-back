package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3683278580183375969L;

	public NotFoundException(String message) {
		super(message);
	}
}
