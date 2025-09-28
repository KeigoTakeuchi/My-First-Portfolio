package com.example.demo.repository;

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
	
	Integer insertImages(List<Image> images);
	
	Integer updatePathName(Image image);
	
	Integer deleteImageById(@Param("id")Integer id);
	
	Integer deleteImagesByMessageId(@Param("messageId")Integer messageId);
	
	Integer deleteImagesByMessageIds(List<Integer> messageId);
}
