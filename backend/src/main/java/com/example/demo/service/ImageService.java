package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Image;

public interface ImageService {
	
	Image getImage(Integer id);
	
	List<Image> getImagesByMessage(Integer messageId);
	
	List<Image> getImagesByAccount(Integer accountId);
	
	void postImage(Image image,Integer messageId);
	
	void changePathName(Image image);
	
	void deleteImage(Image image);
	
	void deleteImageByMessage(Image image, Integer messageId);
}
