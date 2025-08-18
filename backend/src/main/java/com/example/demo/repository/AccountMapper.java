package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Account;

@Mapper
public interface AccountMapper {
	
	Account getAccountById(Integer id);
	
	Integer insertAccount(Account account);
	
	Integer updateAccountName(Account account);
	
	Integer updatePassword(Account account);
	
	Integer updateDisplayName(Account account);
	
	Integer updateAuthority(Account account);
	
	Integer deleteAccountById(Account account);
	
}
