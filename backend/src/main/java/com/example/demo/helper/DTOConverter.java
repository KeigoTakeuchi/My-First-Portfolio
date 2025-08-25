package com.example.demo.helper;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.ImageDTO;
import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Image;
import com.example.demo.entity.Message;

public class DTOConverter {

	public static MessageViewDTO convertToMessageViewDTO(Message message) {
		List<ImageDTO> imageDTO = new ArrayList<>();
		
		//Message内にImage情報がある = Messageの詳細表示の場合
		if (message.getImages() != null && !message.getImages().isEmpty()) {
			for(Image image : message.getImages()) {
				//AWS S3実装でパス指定する際はここを修正して
				 imageDTO.add(new ImageDTO(image.getFilePath()));
			}
		}

		
		MessageViewDTO messageViewDTO = MessageViewDTO.builder()
				.id(message.getId())
				.displayName(message.getAccount().getDisplayName())
				.title(message.getTitle())
				.content(message.getContent())
				.images(imageDTO)
				.build();
		return messageViewDTO;
	}
	
	public static Message convertToMessage(MessageFormDTO messageForm,Account account) {
		
		Message message = Message.builder()
				.title(messageForm.getInputTitle())
				.content(messageForm.getInputContent())
				.account(account)
				.build();
		return message;
	}
}
