package com.example.demo.repository;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.Account;

@Mapper
public interface AccountMapper {
	
	Optional<Account> getAccountById(Integer id);
	
	Optional<Account> getAccountByName(String name);
	
	Optional<Account> getAccountByDisplayName(String displayName);
	
	Integer insertAccount(Account account);
	
	Integer updateAccount(Account account);
	
	Integer deleteAccountById(Account account);
	
}
