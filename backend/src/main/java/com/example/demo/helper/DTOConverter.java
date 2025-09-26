package com.example.demo.helper;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.dto.AccountRegisterFormDTO;
import com.example.demo.dto.AccountUpdateFormDTO;
import com.example.demo.dto.AccountViewDTO;
import com.example.demo.dto.ImageDTO;
import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Image;
import com.example.demo.entity.Message;
import com.example.demo.entity.Role;

public class DTOConverter {

	/*
	 * Message
	 */
	public static MessageViewDTO convertToMessageViewDTO(Message message) {
		List<ImageDTO> imagesDTO = new ArrayList<>();
		
		//Message内にImage情報がある = Messageの詳細表示の場合
		if (message.getImages() != null && !message.getImages().isEmpty()) {
			for(Image image : message.getImages()) {
				 imagesDTO.add(new ImageDTO(image.getFilePath()));
			}
		}

		
		MessageViewDTO messageViewDTO = MessageViewDTO.builder()
				.id(message.getId())
				.displayName(message.getAccount().getDisplayName())
				.title(message.getTitle())
				.content(message.getContent())
				.createdAt(message.getCreatedAt())
				.updatedAt(message.getUpdatedAt())
				.images(imagesDTO)
				.account(message.getAccount())
				.build();
		return messageViewDTO;
	}
	
	public static Message convertToMessage(MessageViewDTO viewDTO) {
		
		//MessageViewDTOには参照関係のEntityを保持できないため、Service層などで格納してあげる必要あり
		Message message = Message.builder()
				.id(viewDTO.getId())
				.title(viewDTO.getTitle())
				.content(viewDTO.getContent())
				.createdAt(viewDTO.getCreatedAt())
				.updatedAt(viewDTO.getUpdatedAt())
				.build();
		return message; 
	}
	
	public static Message convertToMessage(MessageFormDTO messageForm,Account account) {
		
		Message message = Message.builder()
				.title(messageForm.getInputTitle())
				.content(messageForm.getInputContent())
				.account(account)
				.build();
		return message;
	}
	
	/*
	 * Account
	 */
	
	//defaultではAccountは登録された場合、RoleはUSERになる
	public static Account convertToAccountByRegisterDTO(AccountRegisterFormDTO accountForm) {
		
		Account account = Account.builder()
				.name(accountForm.getInputName())
				.displayName(accountForm.getInputDisplayName())
				.authority(Role.USER)
				.build();
		return account;
	}
	
	public static Account convertToAccountByUpdateDTO(AccountUpdateFormDTO accountForm) {
		
		Account account = Account.builder()
				.id(accountForm.getId())
				.name(accountForm.getInputName())
				.displayName(accountForm.getInputDisplayName())
				.authority(accountForm.getAuthority())
				.build();
		
		return account;
	}
	
	public static AccountViewDTO convertToAccountViewDTOByAccount(Account account) {
		
		AccountViewDTO viewDTO = AccountViewDTO.builder()
				.id(account.getId())
				.displayName(account.getDisplayName())
				.authority(account.getAuthority())
				.createdAt(account.getCreatedAt())
				.updatedAt(account.getUpdatedAt())
				.deletedAt(account.getDeletedAt())
				.build();
		
		return viewDTO;
	}
	

}
