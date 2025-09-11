package com.example.demo.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.example.demo.validate.UnusedName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class LoginFormDTO {

	@Size(min = 8,max = 20,message = "IDは{min}～{max}文字以内にしてください")
	@Pattern(regexp = "[a-z0-9]",message = "IDは半角小文字英字、半角数字のみにしてください")
	@UnusedName
	private String inputName;
	
	@Size(min = 8,max = 20,message = "パスワードは{min}～{max}文字以内にしてください")
	@Pattern(regexp = "[a-z0-9]",message = "パスワードは半角小文字英字、半角数字のみにしてください")
	private String inputPassword;
}
