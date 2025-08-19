package com.example.demo.service.Impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Account;
import com.example.demo.entity.Message;
import com.example.demo.repository.AccountMapper;
import com.example.demo.repository.MessageMapper;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

	private final MessageMapper messageMapper;
	private final AccountMapper accountMapper;
	
	public List<Message>  getAllMessages(){
		
		List<Message> messages = messageMapper.getAllMessages();
		
		if(messages.isEmpty()) {
			return new ArrayList<Message>(); 
		}
		return messages;
	}
	
	public List<Message> getAllMessagesByAccountId(Integer accountId){
		List<Message> messages = messageMapper.getAllMessagesByAccountId(accountId);
		
		if (messages.isEmpty()) {
			return new ArrayList<Message>();
		}
		return messages;
	}
	
	public List<Message> searchMessagesByWord(String word){
		
		if (word == null || word.trim().isEmpty()) {
			return Collections.emptyList();
		}
		
		String searchQuery = "%" + word + "%";
		
		List<Message> searchLists = messageMapper.getMessagesByWord(searchQuery);
		return searchLists;
	}
	
	public void postMessage(Message message,Integer accountId) {
		Account account = accountMapper.getAccountById(accountId);
		//MessageはDTOに替えたのち、下で補完してあげる必要あり
		message.setAccount(account);
		message.setAccountId(account.getId());
		messageMapper.insertMessage(message);
	}
	
	public void changeTitle(Message message) {
		
		messageMapper.updateTitleById(message);
	}
	
	public void changeContent(Message message) {
		messageMapper.updateContentById(message);
	}
	
	public void deleteMessages(Message message,Integer id) {
		LocalDateTime updatedAt = message.getUpdatedAt();
		messageMapper.deleteAllMessagesByAccountId(id, updatedAt);
	}
	
	public void deleteAllMessagesByAccount(Message message,Integer accountId) {
		LocalDateTime updatedAt = message.getUpdatedAt();
		messageMapper.deleteAllMessagesByAccountId(accountId,updatedAt);
	}
}
