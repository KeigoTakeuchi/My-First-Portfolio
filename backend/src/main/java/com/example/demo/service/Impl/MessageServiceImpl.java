package com.example.demo.service.Impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.MessageFormDTO;
import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Account;
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

	private final MessageMapper messageMapper;
	private final ImageService imageService;
	
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
		return messageMapper.getMessageIdByAccountId(accountId);
	}
	
	//Valid通過後のハンドラメソッド内でScopeからAccountIdを取得する
	public MessageViewDTO postMessage(MessageFormDTO messageForm,Account account,List<MultipartFile> images) {		
		//Formから画像を受け取った場合の処理
		if(images != null && !images.isEmpty()) {
			/*ここでMultipartFile型のDTOをUUIDで自動生成した文字列とファイル名(MultipartFile.getOriginalFileName()で取得可)を
			 * 組み合わせたオブジェクトキーを紐づけてfilePathプロパティに格納(例:/images/user/afhd2442jlnj24k.jpg)
			 * して、MessageForm内のList<MultipartFile> imagesをImageEntityとして補完していき、完成したImageEntityのListを保持しておく
			 */
			/*for (MultipartFile multiFile : images) {
				multiFile.getOriginalFilename()
			}
			*/
		}
		//取得したAccountEntityとFormから受け取った変数をもとにMessageEntityへ変換(Imageは別で変換させる)
		Message message = DTOConverter.convertToMessage(messageForm, account);

		messageMapper.insertMessage(message.getAccount().getId(),message);
		
		//上で作成したList型ImageEntityからImageEntity抜き出してDB保存(ここではList型をimagesとして仮作成)
		/*for (Image image : images) {
			imageMapper.insertImage(message.getId(),image);
		}
		*/
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
		
		
		imageService.deleteImageByMessage(messageId);
		
		messageMapper.deleteMessageById(messageId, message.getUpdatedAt());
		
	}
	
	public void deleteAllMessagesByAccount(Integer accountId) {
		messageMapper.deleteAllMessagesByAccountId(accountId);
	}
}
