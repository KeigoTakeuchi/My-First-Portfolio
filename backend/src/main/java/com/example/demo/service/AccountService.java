package com.example.demo.service;

import com.example.demo.dto.AccountPasswordUpdateForm;
import com.example.demo.dto.AccountRegisterFormDTO;
import com.example.demo.dto.AccountUpdateFormDTO;
import com.example.demo.dto.AccountViewDTO;
import com.example.demo.entity.Account;

public interface AccountService {

	Account getAccount(Integer accountId);
	
	AccountViewDTO getAccountForView(Integer accountId);
	
	Account findAccountByName(String name);
	
	Account findAccountByDisplayName(String displayName);
	
	void registerAccount(AccountRegisterFormDTO accountForm);
	
	void changeAccount(AccountUpdateFormDTO accountForm);
	
	void changePassword(AccountPasswordUpdateForm passForm);
		
	void deleteAccount(Integer accountId);
}
