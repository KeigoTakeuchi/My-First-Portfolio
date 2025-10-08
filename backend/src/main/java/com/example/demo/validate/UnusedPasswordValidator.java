package com.example.demo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.dto.AccountPasswordUpdateForm;
import com.example.demo.entity.Account;
import com.example.demo.repository.AccountMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UnusedPasswordValidator implements ConstraintValidator<UnusedPassword, AccountPasswordUpdateForm> {

	private final AccountMapper mapper;
	private final PasswordEncoder encoder;
	
	public void initialize(UnusedPassword constraintAnnotation) {
		
	}
	
	public boolean isValid(AccountPasswordUpdateForm value, ConstraintValidatorContext context) {
		
		if(value.getInputNewPassword() == null || value.getInputOldPassword() == null) {
			//nullはNotBlankに任せるためtrueで返す
			return true;
		}
		
		Account account = mapper.getAccountById(value.getId())
				.orElse(null);
		
		if(account == null) {
			return false;
		}
		
		if (encoder.matches(value.getInputNewPassword(),account.getHashedPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("以前のパスワードが入力されています")
                   .addPropertyNode("inputNewPassword") // 確認用パスワードにエラー付与
                   .addConstraintViolation();
			return false;
		}
		return true;
	}
}
