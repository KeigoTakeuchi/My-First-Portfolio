package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AccountRegisterFormDTO;
import com.example.demo.dto.LoginFormDTO;
import com.example.demo.service.auth.AuthenticationService;
import com.example.demo.service.auth.AuthenticationService.AuthenticationResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;
	
	
	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponse> register(
			@RequestBody AccountRegisterFormDTO accountForm){
		
		return ResponseEntity.ok(service.register(accountForm));
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticationResponse> authenticate(
			@RequestBody LoginFormDTO formDTO){
		
		return ResponseEntity.ok(service.authenticate(formDTO));
	}
}
