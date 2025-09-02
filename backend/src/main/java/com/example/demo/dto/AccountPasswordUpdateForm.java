package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.example.demo.validate.ConfirmPassword;
import com.example.demo.validate.UnusedPassword;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@UnusedPassword
@ConfirmPassword
public class AccountPasswordUpdateForm {

	private int id;
	
	@NotBlank(message = "以前のパスワードを入力してください")
	@Size(min = 8,max = 20,message = "パスワードは{min}～{max}文字以内にしてください")
	@Pattern(regexp = "")
	private String inputOldPassword;
	
	@NotBlank(message = "新しいパスワードを入力してください")
	@Size(min = 8,max = 20,message = "パスワードは{min}～{max}文字以内にしてください")
	@Pattern(regexp = "")
	private String inputNewPassword;
	
	@NotBlank(message = "確認用パスワードを入力してください")
	private String inputConfirmPassword;
}
