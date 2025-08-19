package com.example.demo.service;

import com.example.demo.entity.Account;

public interface AccountService {

	Account getAccount(Integer accountId);
	
	void registerAccount(Account account);
	
	void changeName(Account account);
	
	void changePassword(Account account);
	
	void changeDisplayName(Account account);
	
	void updateAuthority(Account account);
	
	void deleteAccount(Account account);
}
