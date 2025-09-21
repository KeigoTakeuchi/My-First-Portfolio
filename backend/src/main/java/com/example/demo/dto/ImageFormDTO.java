package com.example.demo.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.validate.MediaTypeImage;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ImageFormDTO {

	private List<@MediaTypeImage MultipartFile> files;
	
}
