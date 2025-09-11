package com.example.demo.dto;

import java.time.LocalDateTime;

import com.example.demo.entity.Role;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AccountViewDTO {
	
	private int id;
	
	private String displayName;
	
	private Role authority;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime updatedAt;
	
	private LocalDateTime deletedAt;

}
