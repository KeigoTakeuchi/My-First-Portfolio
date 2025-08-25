package com.example.demo.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Image;
import com.example.demo.service.ImageService;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	
	@Override
	public Image getImage(Integer id) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<Image> getImagesByMessage(Integer messageId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<Image> getImagesByAccount(Integer accountId) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void postImage(Image image, Integer messageId) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void changePathName(Image image) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void deleteImage(Image image) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public void deleteImageByMessage(Image image, Integer messageId) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
