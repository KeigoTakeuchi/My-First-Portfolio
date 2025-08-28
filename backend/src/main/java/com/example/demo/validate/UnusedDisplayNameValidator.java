package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;

public class UnusedDisplayNameValidator implements ConstraintValidator<UnusedDisplayName, String> {
	
	private AccountService service;
	
	public void initialize(UnusedDisplayName contraintAnnotation) {
		
	}
	
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Account account = service.findAccountByDisplayName(value);
		
		if(account == null) {
			return true;
		}
		return false;
	}
}
