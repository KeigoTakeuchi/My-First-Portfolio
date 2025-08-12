package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
	
	private int id;
	
	private String name;
	
	private String path_name;
	
	private LocalDateTime created_at;
	
	private LocalDateTime updated_at;
	
	private Account account;
}
