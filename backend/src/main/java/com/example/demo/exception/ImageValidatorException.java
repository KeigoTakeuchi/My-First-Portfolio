package com.example.demo.exception;

public class ImageValidatorException extends RuntimeException{

	public ImageValidatorException(String message) {
		super(message);
	}
	
	public ImageValidatorException(String message, Throwable cause) {
		super(message,cause);
	}
}
