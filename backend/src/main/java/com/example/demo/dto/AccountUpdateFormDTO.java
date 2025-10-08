package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.example.demo.entity.Role;
import com.example.demo.validate.UnusedDisplayName;
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
public class AccountUpdateFormDTO {

	private  int id;
	
	@NotBlank(message="IDは入力してください")
	@Size(min = 8,max = 20,message = "IDは{min}～{max}文字以内にしてください")
	@Pattern(regexp = "^[a-z0-9]+$",message = "IDは小文字の半角英字、半角数字のみにしてください")
	@UnusedName
	private String inputName;
	
	@NotBlank(message="表示名は入力してください")
	@Size(min = 1,max = 50,message = "表示名は{min}～{max}文字以内にしてください")
	@UnusedDisplayName
	private String inputDisplayName;
	
	private Role authority;
}
