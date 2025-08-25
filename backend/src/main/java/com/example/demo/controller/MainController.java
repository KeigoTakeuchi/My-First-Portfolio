package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Message;
import com.example.demo.service.AccountService;
import com.example.demo.service.ImageService;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MainController {
	
	private final AccountService accountService;
	private final MessageService messageService;
	private final ImageService imageService;
	
	@GetMapping
	public List<MessageViewDTO> getMessages() {
		return messageService.getAllMessages();
	}
	
	@GetMapping("/{id}")
	public Message getMessage(@PathVariable Integer id) {
		return messageService.getMessage(id);
	}
	
	
}
