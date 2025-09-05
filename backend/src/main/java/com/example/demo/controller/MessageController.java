package com.example.demo.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;
import com.example.demo.service.AccountService;
import com.example.demo.service.ImageService;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {
	
	private final AccountService accountService;
	private final MessageService messageService;
	private final ImageService imageService;
	
	//一覧表示(全ユーザー)
	@GetMapping("/messages")
	public List<MessageViewDTO> getMessages() {
		return messageService.getAllMessages();
	}
	
	//詳細表示(一投稿)
	@GetMapping("/messages/{id}")
	public MessageViewDTO getMessage(@PathVariable Integer id) {
		return messageService.getMessage(id);
	}
	
	//一覧表示(特定のユーザー)
	@GetMapping("/users/{id}/messages")
	public List<MessageViewDTO> getMessageByAccount(@PathVariable Integer id){
		return messageService.getAllMessagesByAccountId(id);
	}
	
	//投稿検索
	@GetMapping("/messages/search")
	public List<MessageViewDTO> getMessagesByWord(@RequestParam("keyword") String word){
		return messageService.searchMessagesByWord(word);
	}
	
	//新規投稿処理
	@PostMapping("/messages")
	public ResponseEntity<MessageViewDTO> registerMessage(
			@Validated @RequestPart("") MessageFormDTO messageFormDTO,
			@RequestPart("") List<MultipartFile> images,
			@AuthenticationPrincipal UserDetails userDetails){
		
		
		MessageViewDTO viewDTO = messageService.postMessage(messageFormDTO, userDetails.getUsername(),images);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequestUri()
				.path("/{id}")
				.buildAndExpand(viewDTO.getId())
				.toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping("/messages/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateMessage(@PathVariable Integer messageId,
			@Validated @RequestBody MessageFormDTO formDTO,
			@AuthenticationPrincipal UserDetails userDetails
			) {
		String userName = userDetails.getUsername();
		
		messageService.changeMessage(formDTO,messageId,userName);
	}
	
	@DeleteMapping("/messages/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMessage(@PathVariable Integer messageId,
			@AuthenticationPrincipal UserDetails userDetails) {
		String userName = userDetails.getUsername();
		messageService.deleteMessages(messageId,userName);
	}
}
