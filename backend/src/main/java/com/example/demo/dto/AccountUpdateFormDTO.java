package com.example.demo.dto;

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
	
	@Size(min = 8,max = 20,message = "IDは{min}～{max}文字以内にしてください")
	@Pattern(regexp = "")
	@UnusedName
	private String inputName;
	
	private String password;
	
	@Size(min = 1,max = 50,message = "表示名は{min}～{max}文字以内にしてください")
	@UnusedDisplayName
	private String inputDisplayName;
	
	private Role authority;
}
