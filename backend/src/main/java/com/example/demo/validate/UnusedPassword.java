package com.example.demo.validate;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UnusedPasswordValidator.class)
@Retention(RUNTIME)
@Target({TYPE})
public @interface UnusedPassword {
	
	String message() default "既に登録済みのパスワードです";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};

	@Target({TYPE})
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		UnusedPassword[] value();
	}
}
