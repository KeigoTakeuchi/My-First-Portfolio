package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Message {

	private int id;
	
	private String title;
	
	private String content;
	
	private LocalDateTime created_at;
	
	private LocalDateTime updated_at;
	
	private Account account;
	
	private List<Image> images;
}
