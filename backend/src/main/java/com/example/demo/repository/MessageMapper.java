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
	
	Integer insertMessage(Message message);
	
	Integer updateTitleById(Message message);
	
	Integer updateContentById(Message message);
	
	Integer deleteMessageById(@Param("id") Integer id, @Param("updatedAt") LocalDateTime updatedAt);
	
	Integer deleteAllMessagesByAccountId(@Param("accountId") Integer id, @Param("updatedAt")LocalDateTime updatedAt);
	
}
