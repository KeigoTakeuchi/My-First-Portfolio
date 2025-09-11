package com.example.demo.service.Impl;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Account;
import com.example.demo.entity.LoginUser;
import com.example.demo.repository.AccountMapper;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class LoginUserDetailsServiceImpl implements UserDetailsService {

	private final AccountMapper mapper ;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO 自動生成されたメソッド・スタブ
		Account account = mapper.getAccountByName(username);
		
		List<GrantedAuthority> authorities = account.getAuthority().getAuthorities();
		
		if(account != null) {
			return new LoginUser(account.getName(),account.getHashedPassword(),
					authorities,account.getId(),account.getDisplayName());
		}else {
			throw new UsernameNotFoundException(username + "指定しているユーザー名は存在しません");
		}
	}
	

}
