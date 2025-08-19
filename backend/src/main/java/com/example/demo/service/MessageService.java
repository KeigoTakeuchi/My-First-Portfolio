package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Message;

public interface MessageService {

	List<Message> getAllMessages();
	
	List<Message> getAllMessagesByAccountId(Integer accountId);
	
	List<Message> searchMessagesByWord(String word);
	
	void postMessage(Message message,Integer accountId);
	
	void changeTitle(Message message);
	
	void changeContent(Message message);
	
	void deleteMessages(Message message,Integer id);
	
	void deleteAllMessagesByAccount(Message message,Integer accountId);
}
