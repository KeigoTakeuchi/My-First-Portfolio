package com.example.demo.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.MessageViewDTO;
import com.example.demo.entity.Image;
import com.example.demo.entity.Message;
import com.example.demo.helper.DTOConverter;
import com.example.demo.repository.ImageMapper;
import com.example.demo.service.ImageService;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageMapper imageMapper;
	private final MessageService messageService;  
	
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

	//以下delete操作ではAWS S3のストレージ内ファイルも削除する処理を実装する必要あり
	@Override
	public void deleteImage(Image image) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/*①メッセージ投稿一覧から@PathVariableで削除する場合
	 * ②画像のみを削除する場合
	 */
	@Override
	public void deleteImageByMessage(Integer messageId) {
		// TODO 自動生成されたメソッド・スタブ
		MessageViewDTO messageViewDTO = messageService.getMessage(messageId);
		
		Message message = DTOConverter.convertToMessage(messageViewDTO);
		imageMapper.deleteImagesByMessageId(messageId,message.getUpdatedAt());
	}
	
	
	/*AWS S3内のデータにアクセスし削除処理をしたのちにDB削除をする
	 * AWS関連の処理は後に実装予定
	*/	
	public void deleteImagesByAccountId(Integer accountId) {
		
		List<Integer> messageIds = messageService.getMessageIdByAccountId(accountId);
		
		if(messageIds != null && !messageIds.isEmpty()) {
			imageMapper.deleteImagesByMessageIds(messageIds);
		}
		return;
	}

}
