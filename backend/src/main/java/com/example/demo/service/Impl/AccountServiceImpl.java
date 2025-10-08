package com.example.demo.service.Impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AccountPasswordUpdateForm;
import com.example.demo.dto.AccountUpdateFormDTO;
import com.example.demo.dto.AccountViewDTO;
import com.example.demo.entity.Account;
import com.example.demo.exception.DataConflictException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.helper.DTOConverter;
import com.example.demo.repository.AccountMapper;
import com.example.demo.service.AccountService;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	
	private final AccountMapper accountMapper;
	private final MessageService messageService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Account getAccount(Integer accountId) {
		// TODO 自動生成されたメソッド・スタブ
		Account account = accountMapper.getAccountById(accountId)
				.orElseThrow(() ->  new ResourceNotFoundException("Accountが存在しません。 ID : " + accountId));
		
		return account;
	}
	
	public AccountViewDTO getAccountForView(Integer accountId) {
		
		Account account = accountMapper.getAccountById(accountId)
				.orElseThrow(() ->  new ResourceNotFoundException("Accountが存在しません。 ID : " + accountId));
	
		AccountViewDTO accountViewDTO = DTOConverter.convertToAccountViewDTOByAccount(account);
		
		return accountViewDTO;
	}
	
	public Account findAccountByName(String name) {
		Account account = accountMapper.getAccountByName(name)
				.orElse(null);
		
		return account;
	}

	public Account findAccountByDisplayName(String displayName) {
		Account account = accountMapper.getAccountByDisplayName(displayName)
				.orElse(null);
		
		return account;
	}

	@Override
	public void changeAccount(Integer targetId,AccountUpdateFormDTO accountForm) {
		// TODO 自動生成されたメソッド・スタブ
		
		Account account = accountMapper.getAccountById(targetId)
				.orElseThrow(() -> new ResourceNotFoundException("Accountが存在しません。 ID : " + targetId));
		
		
		//入力のあったフィールド(変更したいフィールド)のみupdateさせる為の処理
		Optional.ofNullable(accountForm.getInputName())
			.filter(s -> !s.isEmpty())
			.ifPresent(account::setName);
		
		Optional.ofNullable(accountForm.getInputDisplayName())
			.filter(s -> !s.isEmpty())
			.ifPresent(account::setDisplayName);
		
		int result = accountMapper.updateAccount(account);
		
		//account内updatedAtによる楽観的ロックで異常を検知した場合に、"競合した"という形でイベント消化している
		if(result != 1) {
			throw new DataConflictException("データの更新に失敗しました。 ID : " + targetId);
		}
	}
	
	public void changePassword(Integer targetId,AccountPasswordUpdateForm passForm) {

		
		passForm.setId(targetId);
		
		Account account = accountMapper.getAccountById(passForm.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Accountが存在しません。 ID : " + passForm.getId()));
		
		
		String inputNewPassword = passForm.getInputNewPassword();
		String inputHashedPassword = passwordEncoder.encode(inputNewPassword);
		
		account.setHashedPassword(inputHashedPassword);
		
		Integer result = accountMapper.updateAccount(account);
		
		if (result != 1) {
			throw new DataConflictException("データの更新に失敗しました。 ID : " + passForm.getId());
		}
	}

	@Override
	public void deleteAccount(Integer targetId) {
		// TODO 自動生成されたメソッド・スタブ	
		
		//AccountMapperにはMessage,Imageの情報を何も持たない
		Account account = accountMapper.getAccountById(targetId)
				.orElseThrow(() -> new ResourceNotFoundException("Accountが存在しません。 ID : " + targetId));
		
		messageService.deleteAllMessagesByAccount(targetId);
		
		Integer result = accountMapper.deleteAccountById(account);
		
		if (result != 1) {
			throw new DataConflictException("データの削除に失敗しました。 ID : " + targetId);
		}
	}
}
