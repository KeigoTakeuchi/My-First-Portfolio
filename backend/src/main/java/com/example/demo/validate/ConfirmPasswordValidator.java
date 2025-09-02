package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.demo.dto.AccountPasswordUpdateForm;


public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, AccountPasswordUpdateForm> {

	public void initialize(ConfirmPassword constraintAnnotation) {
		
	}
	
	public boolean isValid(AccountPasswordUpdateForm value,ConstraintValidatorContext context) {
		
		if(value.getInputConfirmPassword() == null || value.getInputNewPassword() == null) {
			return true;
		}
		
		if(!value.getInputNewPassword().equals(value.getInputConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("新しいパスワードと確認用パスワードが一致しません")
                   .addPropertyNode("inputConfirmPassword") // エラーを確認用パスワードに付与
                   .addConstraintViolation();
			return false;
		}
		
		return true;
	}
}
