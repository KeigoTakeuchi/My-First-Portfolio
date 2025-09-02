package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.Image;

@Mapper
public interface ImageMapper {

	Image getImageById(Integer id);
	
	List<Image> getImagesByMessageId(Integer id);
	
	List<Image> getImagesByAccountId(Integer id);
	
	Integer insertImage(@Param("messageId")Integer messageId,Image image);
	
	Integer updatePathName(Image image);
	
	Integer deleteImageById(@Param("id")Integer id, @Param("updatedAt")LocalDateTime updatedAt);
	
	Integer deleteImagesByMessageId(@Param("messageId")Integer messageId,@Param("updatedAt") LocalDateTime updatedAt);
	
	Integer deleteImagesByMessageIds(List<Integer> messageId);
}
