package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Account;

@Mapper
public interface AccountMapper {
	
	Account getAccountById(Integer id);
	
	Account getAccountByName(String name);
	
	Account getAccountByDisplayName(String displayName);
	
	Integer insertAccount(Account account);
	
	Integer updateAccount(Account account);
	
	Integer deleteAccountById(Account account);
	
}
