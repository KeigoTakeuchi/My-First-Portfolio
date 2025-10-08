package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UnusedDisplayNameValidator implements ConstraintValidator<UnusedDisplayName, String> {
	
	private final AccountService service;
	
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
