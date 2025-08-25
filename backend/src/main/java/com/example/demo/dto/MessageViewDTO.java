package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MessageViewDTO {
	
	private int id;

	private String displayName;
	
	private String title;
	
	private String content;
	
	private List<ImageDTO> images;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
