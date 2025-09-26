package com.example.demo.validate;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Retention(RUNTIME)
@Constraint(validatedBy = MediaTypeImageValidator.class)
@Target({METHOD,FIELD,TYPE_USE})
public @interface MediaTypeImage {
	
	String message() default "ファイル形式が正しくありません";
	
	Class<?>[] groups() default{};
	
	Class<? extends Payload>[] payload() default{};
	
	@Documented
	@Retention(RUNTIME)
	@Target({METHOD,FIELD,TYPE_USE})
	public @interface List {
		MediaTypeImage[] value();
	}

}
