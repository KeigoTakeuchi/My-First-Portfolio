package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Image {
	
	private int id;
	
	private String name;
	
	private String filePath;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private Message message;
}
