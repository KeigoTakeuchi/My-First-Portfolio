package com.example.demo.service.Impl;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Account;
import com.example.demo.entity.Image;
import com.example.demo.entity.Message;
import com.example.demo.helper.DTOConverter;
import com.example.demo.repository.MessageMapper;
import com.example.demo.service.ImageService;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
	
	//デバッグ
	private static final Logger logger = LoggerFactory.getLogger(StorageService.class);

	private final MessageMapper messageMapper;
	private final ImageService imageService;
	private final StorageService storageService;
	
	public List<MessageViewDTO> getAllMessages(){
		
		List<Message> messages = messageMapper.getAllMessages();
		
		if(messages.isEmpty()) {
			return new ArrayList<MessageViewDTO>(); 
		}
		
		List<MessageViewDTO> messageViewList = new ArrayList<MessageViewDTO>();
		
		for(Message message : messages) {
			
			messageViewList.add(DTOConverter.convertToMessageViewDTO(message));
		}
		return messageViewList;
	}
	
	public List<MessageViewDTO> getAllMessagesByAccountId(Integer accountId){
		List<Message> messages = messageMapper.getAllMessagesByAccountId(accountId);
		
		if (messages.isEmpty()) {
			return new ArrayList<MessageViewDTO>();
		}
		
		List<MessageViewDTO> messageViewList = new ArrayList<>();
		
		for (Message message : messages) {
			
			messageViewList.add(DTOConverter.convertToMessageViewDTO(message));
		}
		return messageViewList;
	}
	
	//修正の必要ありかも(今後)
	//Messageの詳細画面にはAccount,ImageのDTO情報をもつ
	public MessageViewDTO getMessage(Integer id) {
		Message message = messageMapper.getMessageById(id);
		MessageViewDTO messageViewDTO = DTOConverter.convertToMessageViewDTO(message);
		return messageViewDTO;
	}
	
	public List<MessageViewDTO> searchMessagesByWord(String word){
		
		if (word == null || word.trim().isEmpty()) {
			return Collections.emptyList();
		}
		
		String searchQuery = "%" + word + "%";
		
		List<Message> searchList = messageMapper.getMessagesByWord(searchQuery);
		
		List<MessageViewDTO> messageViewList = new ArrayList<>();
		
		for(Message message : searchList) {
			MessageViewDTO messageViewDTO = DTOConverter.convertToMessageViewDTO(message);
			messageViewList.add(messageViewDTO);
		}
		return messageViewList;
	}
	
	public List<Integer> getMessageIdByAccountId(Integer accountId){
		List<Message> messages = messageMapper.getMessageIdByAccountId(accountId);
		
		List<Integer> messageIds = new ArrayList<>();
		
		for (Message message : messages) {
			messageIds.add(message.getId());
		}
		
		return messageIds;
	}
	
	//Valid通過後のハンドラメソッド内でScopeからAccountIdを取得する
	public MessageViewDTO postMessage(MessageFormDTO messageForm,Account account,List<MultipartFile> images){
		
		//ここではImageをmessageEntityに変換していない(中身が空)
		Message message = DTOConverter.convertToMessage(messageForm, account);

		messageMapper.insertMessage(message.getAccount().getId(),message);
		
		
		//Formから画像を受け取った場合の処理
		if(images != null && images.size() > 4) {
			throw new IllegalArgumentException("画像は4枚までです"); 
		}
		
		//画像登録処理
		List<Image> imageEntities = new ArrayList<>();
		
		if (images != null) {
			
			for (MultipartFile image : images) {
				
				if (image.isEmpty()) continue ;
				
				String ext = FilenameUtils.getExtension(image.getOriginalFilename());
				//uuidはimageオブジェクト一つに対して生成
				String uuid = UUID.randomUUID().toString();
				byte[] imageBytes = convertFileToBytes(image);
				
				//logger.info("storageService呼び出し前のimage contentType :{}",image.getContentType());
				
				//supabaseへ画像をアップロード
				String objectPath = storageService.upload(account.getId(),message.getId(),uuid,ext, imageBytes, image.getContentType());
				
				//metadataをDBに保存
				Image imageEntity = Image.builder()
						.name(image.getName())
						.filePath(objectPath)
						.messageId(message.getId())
						.build();
				
				imageEntities.add(imageEntity);
			}
			imageService.postAllImages(imageEntities);
		}
		
		message.setImages(imageEntities);
		MessageViewDTO viewDTO = DTOConverter.convertToMessageViewDTO(message);
		
		return viewDTO;
	}
	
	/*ControllerでFormページで送られてきたFormDTOとmessageID(@PAthVariable)をもとにMessageEntityを完成させて
	 * MessageMapperに渡してあげる
	 */
	public void changeMessage(MessageFormDTO messageForm,Integer messageId,String name) {
		//選択されたmessageIdをもとにEntityの準備
		Message message = messageMapper.getMessageById(messageId);
		
		String userName = message.getAccount().getName();
		
		if(!userName.equals(name)) {
			throw new AccessDeniedException("このデータを編集する権限がありません");
		}
		//Formから受け取った値をEntityにset
		message.setTitle(messageForm.getInputTitle());
		message.setContent(messageForm.getInputContent());
		
		//EntityをMapper更新メソッドに渡す
		messageMapper.updateMessageById(message);
	}
	
	
	//一覧表示から消すことを想定
	//Imageの削除処理も要実装
	public void deleteMessages(Integer messageId,String name) {
		
		Message message = messageMapper.getMessageById(messageId);
		
		String userName = message.getAccount().getName();
		
		if(!userName.equals(name)) {
			throw new AccessDeniedException("このデータを編集する権限がありません");
		}
		
		
		if(!message.getImages().isEmpty() && message.getImages() != null) {
			
			List<Image> images = message.getImages();
			
			for(Image image : images) {
				
				storageService.delete(image.getFilePath());
			}
			
			imageService.deleteImageByMessage(messageId);
		}
		
		
		messageMapper.deleteMessageById(messageId, message.getUpdatedAt());
		
	}
	
	//要修正:messageIdsのnullチェックはできてるが、imageのチェックができてない
	public void deleteAllMessagesByAccount(Integer accountId) {
		
		List<Image> images = imageService.getImagesByAccount(accountId);
		
		if(images != null && !images.isEmpty()) {
			
			for(Image image : images) {
				storageService.delete(image.getFilePath());
			}	
		}
		
		List<Integer> messageIds = this.getMessageIdByAccountId(accountId);
			
		if(messageIds != null && !messageIds.isEmpty()) {
			imageService.deleteImagesByMessageIds(messageIds);
		}
		
		//Messageの全削除
		messageMapper.deleteAllMessagesByAccountId(accountId);
	}
	
	public byte[] convertFileToBytes(MultipartFile file) {
		try {
			byte[] bytes = file.getBytes();
			return bytes;
		}catch (IOException e) {
			throw new UncheckedIOException("バイト変換に失敗しました",e);
		}
	}
}
