package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ImageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ImageController {
	
	private final ImageService imageService;

	
	@GetMapping("/images/{id}")
	public ResponseEntity<String> getImage(@PathVariable Integer id){
		return ResponseEntity.ok(imageService.getImage(id).getFilePath());
	}
	
}
