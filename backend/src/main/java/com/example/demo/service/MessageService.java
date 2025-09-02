package com.example.demo.service;

import java.util.List;

import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;

public interface MessageService {

	List<MessageViewDTO> getAllMessages();
	
	List<MessageViewDTO> getAllMessagesByAccountId(Integer accountId);
	
	MessageViewDTO getMessage(Integer id);
	
	List<MessageViewDTO> searchMessagesByWord(String word);
	
	List<Integer> getMessageIdByAccountId(Integer accountId);
	
	void postMessage(MessageFormDTO messageForm,Integer accountId);
	
	void changeMessage(MessageFormDTO messageForm,Integer id);

	void deleteMessages(Integer id);
	
	void deleteAllMessagesByAccount(Integer accountId);
}
