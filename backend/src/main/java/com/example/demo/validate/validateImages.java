package com.example.demo.validate;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.exception.ImageValidatorException;


@Component
public class validateImages {

	public void validate(List<MultipartFile> files) {
		
		if(files == null || files.isEmpty()) {
			return;
		}
		
		//ファイル数のチェック
		if (files.size() > 5) {
			throw new ImageValidatorException("画像は4枚以内にしてください。");
		}
		
		for(MultipartFile file : files) {
			
			if(file.isEmpty()) {
				continue;
			}
			
			//ファイルサイズが5MB以下かのチェック
			if(file.getSize() > 5*1024*1024) {
					throw new ImageValidatorException("ファイルサイズは5MB以下にしてください。");
			}
			
			//ContentTypeのチェック
			String contentType = file.getContentType();
			if(contentType == null || (!contentType.equals("image/png")) && (!contentType.equals("image/jpeg")) 
					&& (!contentType.equals("image/jpg"))) {
				throw new ImageValidatorException("ファイル形式はPNGまたはJPEGのみ許可されています。");
			}
		}
	}
}
