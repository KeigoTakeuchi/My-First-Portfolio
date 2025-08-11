package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Account {
	
	private int id;
	
	private String name;
	
	private String hashed_password;
	
	private String display_name;
	
	private Role authority;
	
	private LocalDateTime created_at;
	
	private LocalDateTime updated_at;
	
	private LocalDateTime deleted_at;
}
