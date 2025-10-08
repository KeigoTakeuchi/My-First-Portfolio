package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AccountPasswordUpdateForm;
import com.example.demo.dto.AccountUpdateFormDTO;
import com.example.demo.dto.AccountViewDTO;
import com.example.demo.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;
	
	@GetMapping("/users/{id}")
	public ResponseEntity<AccountViewDTO> getAccount(@PathVariable Integer id){
		return new ResponseEntity<>(accountService.getAccountForView(id),HttpStatus.OK);
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<String> updateAccount(@PathVariable Integer id,
			@Validated @RequestBody AccountUpdateFormDTO accountForm,
			Authentication authentication) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		Long longAccountId = jwt.getClaim("id");

		Integer loginAccountId = Math.toIntExact(longAccountId); 
		
		if(!loginAccountId.equals(id)) {
			throw new AccessDeniedException("他のユーザーの情報は更新できません。");
		}
		
		accountService.changeAccount(id,accountForm);
		
		return new ResponseEntity<>("データを更新しました",HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/users/{id}/password")
	public ResponseEntity<String> updatePassword(@PathVariable Integer id,
			@Validated @RequestBody AccountPasswordUpdateForm updateForm,
			Authentication authentication) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		Long longAccountId = jwt.getClaim("id");

		Integer loginAccountId = Math.toIntExact(longAccountId); 
		
		if(!loginAccountId.equals(id)) {
			throw new AccessDeniedException("他のユーザーの情報は更新できません。");
		}
		
		accountService.changePassword(id,updateForm);
		
		return new ResponseEntity<>("データを更新しました",HttpStatus.NO_CONTENT);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<String> deleteAccount(@PathVariable Integer id,
			Authentication authentication) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		Long longAccountId = jwt.getClaim("id");

		Integer loginAccountId = Math.toIntExact(longAccountId); 
		
		if(!loginAccountId.equals(id)) {
			throw new AccessDeniedException("他のユーザーの情報は更新できません。");
		}
		
		accountService.deleteAccount(id);
		
		return new ResponseEntity<>("データを削除しました",HttpStatus.NO_CONTENT);
	}
	
	//Debug
	@GetMapping("/debug/auth")
	public Map<String,Object> debugAuth(Authentication auth){
		return Map.of(
				"name" , auth.getName(),
				"authorities" , auth.getAuthorities()
				);
				
	}
}
