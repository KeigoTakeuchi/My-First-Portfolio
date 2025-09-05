package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MessageFormDTO {
	
	
	@NotBlank(message = "タイトルを入力してください")
	@Size(max = 100 , message = "タイトルは{max}文字以内にしてください")
	private String inputTitle;
	
	@NotBlank(message = "テキストを入力してください")
	@Size(max = 500 , message = "テキストは{max}文字以内にしてください")
	private String inputContent;
}

