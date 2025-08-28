package com.example.demo.validate;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = UnusedDisplayNameValidator.class)
@Retention(RUNTIME)
@Target({FIELD})
public @interface UnusedDisplayName {
	
	String message() default "この名前は既に存在しています";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	
	@Target({FIELD})
	@Retention(RUNTIME)
	@Documented
	public @interface List {
		UnusedDisplayName[] value();
	}
}
