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
import org.springframework.web.bind.annotation.ResponseStatus;
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
		return ResponseEntity.ok(accountService.getAccountForView(id));
	}
	
	@PutMapping("/users/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateAccount(@PathVariable Integer id,
			@Validated @RequestBody AccountUpdateFormDTO accountForm,
			Authentication authentication) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		Long longAccountId = jwt.getClaim("id");

		Integer accountId = Math.toIntExact(longAccountId); 
		
		if(!accountId.equals(id)) {
			throw new AccessDeniedException("他のユーザーの情報を更新はできません");
		}
		accountService.changeAccount(id,accountForm);
	}
	
	@PutMapping("/users/{id}/password")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updatePassword(@PathVariable Integer id,
			@Validated @RequestBody AccountPasswordUpdateForm updateForm,
			Authentication authentication) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		Long longAccountId = jwt.getClaim("id");

		Integer accountId = Math.toIntExact(longAccountId); 
		
		if(!accountId.equals(id)) {
			throw new AccessDeniedException("他のユーザーのパスワードは変更できません");
		}
		updateForm.setId(accountId);
		accountService.changePassword(updateForm);
	}
	
	@DeleteMapping("/users/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteAccount(@PathVariable Integer id,
			Authentication authentication) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		Long longAccountId = jwt.getClaim("id");

		Integer accountId = Math.toIntExact(longAccountId); 
		
		if(!accountId.equals(id)) {
			System.out.println("DEBUG: 例外補足でException投げられる前");
			throw new AccessDeniedException("他のユーザーの情報は変更できません");
		}
		accountService.deleteAccount(id);
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
