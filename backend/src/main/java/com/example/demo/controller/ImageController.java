package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ImageService;
import com.example.demo.service.MessageService;
import com.example.demo.service.Impl.StorageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {
	
	private final ImageService imageService;
	private final MessageService messageService;
	private final StorageService storageService;
	
	@GetMapping("/images/{id}")
	public ResponseEntity<String> getImage(@PathVariable Integer id){
		return ResponseEntity.ok(imageService.getImage(id).getFilePath());
	}
	
}
