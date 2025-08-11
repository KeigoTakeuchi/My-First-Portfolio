package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Image {
	
	private int id;
	
	private String name;
	
	private String path_name;
	
	private LocalDateTime created_at;
	
	private LocalDateTime updated_at;
	
	private Account account;
}
