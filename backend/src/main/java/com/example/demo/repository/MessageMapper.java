package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Message;

@Mapper
public interface MessageMapper {
	
	Message getMessageById(Integer id);
	
	List<Message> getAllMessagesByAccountId(Integer id);
	
	List<Message> getAllMessages();
	
	List<Message> getMessagesByWord(@Param("searchQuery") String word);
	
	List<Message> getMessageIdByAccountId(@Param("accountId") Integer accountId);
	
	Integer insertMessage(@Param("accountId") Integer accountId ,Message message);
	
	Integer updateMessageById(Message message);
	
	Integer deleteMessageById(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);
	
	Integer deleteAllMessagesByAccountId(@Param("accountId") Integer accountId);
	
}
