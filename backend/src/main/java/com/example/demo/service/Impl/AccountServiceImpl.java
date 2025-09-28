package com.example.demo.service.Impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AccountPasswordUpdateForm;
import com.example.demo.dto.AccountUpdateFormDTO;
import com.example.demo.dto.AccountViewDTO;
import com.example.demo.entity.Account;
import com.example.demo.helper.DTOConverter;
import com.example.demo.repository.AccountMapper;
import com.example.demo.service.AccountService;
import com.example.demo.service.ImageService;
import com.example.demo.service.MessageService;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
	
	private final AccountMapper accountMapper;
	private final MessageService messageService;
	private final ImageService imageService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Account getAccount(Integer accountId) {
		// TODO 自動生成されたメソッド・スタブ
		Account account = accountMapper.getAccountById(accountId);
		
		//nullの扱いとdeletedAtでの例外処理も
		if(account == null) {
			return null;
		}
		return account;
	}
	
	public AccountViewDTO getAccountForView(Integer accountId) {
		
		Account account = accountMapper.getAccountById(accountId);
		
		if(account == null) {
			return null;
		}
		AccountViewDTO accountViewDTO = DTOConverter.convertToAccountViewDTOByAccount(account);
		//例外処理は後で
		
		return accountViewDTO;
	}
	
	public Account findAccountByName(String name) {
		return accountMapper.getAccountByName(name);
	}

	public Account findAccountByDisplayName(String displayName) {
		return accountMapper.getAccountByDisplayName(displayName);
	}

	@Override
	public void changeAccount(Integer id,AccountUpdateFormDTO accountForm) {
		// TODO 自動生成されたメソッド・スタブ
		/*Accountが削除または存在しない場合の例外処理はあとで実装予定
		*/
		Account account = accountMapper.getAccountById(id);
		//Account account = DTOConverter.convertToAccountByUpdateDTO(accountForm);
		account.setName(accountForm.getInputName());
		account.setDisplayName(accountForm.getInputDisplayName());
		accountMapper.updateAccount(account);
		return ;
	}
	
	public void changePassword(AccountPasswordUpdateForm passForm) {
		Account account = accountMapper.getAccountById(passForm.getId());
		
		
		String inputNewPassword = passForm.getInputNewPassword();
		String inputHashedPassword = passwordEncoder.encode(inputNewPassword);
		
		account.setHashedPassword(inputHashedPassword);
		
		accountMapper.updateAccount(account);
		
		return;
	}

	@Override
	public void deleteAccount(Integer accountId) {
		// TODO 自動生成されたメソッド・スタブ
		
		//AccountMapperにはMessage,Imageの情報を何も持たない
		Account account = accountMapper.getAccountById(accountId);
		
		messageService.deleteAllMessagesByAccount(accountId);
		
		accountMapper.deleteAccountById(account);
		return;
	}

}
