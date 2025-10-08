package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Account;
import com.example.demo.service.AccountService;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {
	
	private final AccountService accountService;
	private final MessageService messageService;
	
	//一覧表示(全ユーザー)
	@GetMapping("/messages")
	public ResponseEntity<List<MessageViewDTO>> getMessages() {
		
		List<MessageViewDTO> messageViewDTO = messageService.getAllMessages();
		
		//投稿が存在しない場合にも200 OKを返す
		return new ResponseEntity<>(messageViewDTO,HttpStatus.OK);
	}
	
	//詳細表示(一投稿)
	@GetMapping("/messages/{id}")
	public ResponseEntity<MessageViewDTO> getMessage(@PathVariable Integer id) {
		
		MessageViewDTO messageViewDTO = messageService.getMessage(id);

		return new ResponseEntity<>(messageViewDTO,HttpStatus.OK);

	}
	
	//一覧表示(特定のユーザー)
	@GetMapping("/users/{id}/messages")
	public ResponseEntity<List<MessageViewDTO>> getMessageByAccount(@PathVariable Integer id){
		
		List<MessageViewDTO> messageViewDTO = messageService.getAllMessagesByAccountId(id);
		
		return new ResponseEntity<>(messageViewDTO,HttpStatus.OK);
	}
	
	//投稿検索
	@GetMapping("/messages/search")
	public ResponseEntity<List<MessageViewDTO>> getMessagesByWord(@RequestParam("keyword") String word){
		
		List<MessageViewDTO> messageViewDTO = messageService.searchMessagesByWord(word);
		
		return new ResponseEntity<>(messageViewDTO,HttpStatus.OK);
	}
	
	//新規投稿処理
	@PostMapping("/messages")
	public ResponseEntity<MessageViewDTO> registerMessage(
			@Validated @RequestPart("jsonFile") MessageFormDTO messageFormDTO,
			@Validated @RequestPart(value= "file" ,required = false) List<MultipartFile> files,
			Authentication authentication){
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		Long longId = jwt.getClaim("id");
		
		Integer accountId = Math.toIntExact(longId);
		
		Account account = accountService.getAccount(accountId);
		
		MessageViewDTO viewDTO = messageService.postMessage(messageFormDTO,account,files);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(viewDTO.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/messages/{messageId}")
	public ResponseEntity<String> updateMessage(@PathVariable Integer messageId,
			@Validated @RequestBody MessageFormDTO formDTO,
			Authentication authentication
			) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		String userName = jwt.getSubject();
		
		messageService.changeMessage(formDTO,messageId,userName);
		
		return new ResponseEntity<>("messageが更新されました",HttpStatus.OK);
	}
	
	@DeleteMapping("/messages/{messageId}")
	public ResponseEntity<String> deleteMessage(@PathVariable Integer messageId,
			Authentication authentication) {
		
		Jwt jwt = (Jwt)authentication.getPrincipal();
		
		String userName = jwt.getSubject();
		messageService.deleteMessages(messageId,userName);
		
		return new ResponseEntity<>("messageが削除されました",HttpStatus.NO_CONTENT);
	}
}
