package com.example.demo.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Image;
import com.example.demo.repository.ImageMapper;
import com.example.demo.service.ImageService;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageMapper imageMapper;
	
	@Override
	public Image getImage(Integer id) {
		// TODO 自動生成されたメソッド・スタブ
		return imageMapper.getImageById(id);
	}

	@Override
	public List<Image> getImagesByMessage(Integer messageId) {
		// TODO 自動生成されたメソッド・スタブ
		return imageMapper.getImagesByMessageId(messageId);
	}

	@Override
	public List<Image> getImagesByAccount(Integer accountId) {
		// TODO 自動生成されたメソッド・スタブ
		return imageMapper.getImagesByAccountId(accountId);
	}

	@Override
	public void postImage(Image image, Integer messageId) {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	public void postAllImages(List<Image> images) {
		Integer result = imageMapper.insertImages(images);
		if(result < 1) {
			throw new IllegalStateException("画像のメタデータが正しくDBに保存されませんでした");
		}
	}

	@Override
	public void changePathName(Image image) {
		// TODO 自動生成されたメソッド・スタブ

	}

	//以下delete操作ではsupabaseのストレージ内ファイルも削除する処理を実装する必要あり
	//単体で画像を消す動作を想定していないため未実装
	@Override
	public void deleteImage(Image image) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/*①メッセージ投稿一覧から@PathVariableで削除する場合
	 * ②画像のみを削除する場合
	 */
	
	//このメソッドは単体で呼び出されることがなく、限定的にMessage削除と共に呼び出される想定
	@Override
	public void deleteImageByMessage(Integer messageId) {
		// TODO 自動生成されたメソッド・スタブ
		//MessageViewDTO messageViewDTO = messageService.getMessage(messageId);
		
		//Message message = DTOConverter.convertToMessage(messageViewDTO);
		
		imageMapper.deleteImagesByMessageId(messageId);
	}
	
	

	public void deleteImagesByMessageIds(List<Integer> messageIds) {
		
		imageMapper.deleteImagesByMessageIds(messageIds);
	}

}
