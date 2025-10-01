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
public class Account {
	
	private int id;
	
	private String name;
	
	private String hashedPassword;
	
	private String displayName;
	
	private Role authority;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
}
