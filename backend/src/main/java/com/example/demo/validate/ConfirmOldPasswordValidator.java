package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.AccountPasswordUpdateForm;
import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ConfirmOldPasswordValidator implements ConstraintValidator<ConfirmOldPassword,AccountPasswordUpdateForm>{

	private final AccountService service;
	private final PasswordEncoder encoder;
	
	public void initialize(ConfirmOldPassword constraintAnnotation) {
	}
	
	public boolean isValid(AccountPasswordUpdateForm value,ConstraintValidatorContext context) {
		
		if(value.getInputOldPassword() == null) {
			//nullはNotBlankに任せるためtrueで返す
			return true;
		}
		
		Account account = service.getAccount(value.getId());
		
		if(account == null) {
			return false;
		}
		
		if (!encoder.matches(value.getInputOldPassword(), account.getHashedPassword())) {
			return false;
		}
		return true;
	}
}
