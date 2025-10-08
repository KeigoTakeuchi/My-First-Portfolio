package com.example.demo.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Account;

public interface MessageService {

	List<MessageViewDTO> getAllMessages();
	
	List<MessageViewDTO> getAllMessagesByAccountId(Integer accountId);
	
	MessageViewDTO getMessage(Integer id);
	
	List<MessageViewDTO> searchMessagesByWord(String word);
	
	List<Integer> getMessageIdByAccountId(Integer accountId);
	
	MessageViewDTO postMessage(MessageFormDTO messageForm,Account account,List<MultipartFile> files) ;
	
	void changeMessage(MessageFormDTO messageForm,Integer messageId,String name);

	void deleteMessages(Integer messageId,String name);
	
	void deleteAllMessagesByAccount(Integer accountId);
}
