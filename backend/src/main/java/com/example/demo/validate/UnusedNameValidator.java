package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;

public class UnusedNameValidator implements ConstraintValidator<UnusedName, String> {

	private AccountService service;
	
	public void initialize(UnusedName constraintAnnotation) {
		
	}
	
	public boolean isValid(String value, ConstraintValidatorContext context) {
		Account account = service.findAccountByName(value);
		if (account == null) {
			return true;
		}
		return false;
	}
}
