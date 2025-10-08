package com.example.demo.validate;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.apache.commons.io.FilenameUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class MediaTypeImageValidator implements ConstraintValidator<MediaTypeImage,MultipartFile>{

	@Override
	public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
		
		if(value.isEmpty()) {
			//空の場合は通す
			return true;
		}
		
		//メディアタイプの取得
		MediaType mediaType = MediaType.parseMediaType(value.getContentType());
		
		//拡張子の取得
		String ext = FilenameUtils.getExtension(value.getOriginalFilename());
		
		//メディアタイプと拡張子の比較検証用List
		List<MediaType> mediaTypeList = Arrays.asList(MediaType.IMAGE_JPEG,MediaType.IMAGE_PNG);
		List<String> extList = Arrays.asList("jpg","png","jpeg");
		
		return mediaTypeList.stream().anyMatch((mType) -> mediaType.includes(mType))
				&& extList.stream().anyMatch((v) -> ext.toLowerCase().equals(v));
	}
}
