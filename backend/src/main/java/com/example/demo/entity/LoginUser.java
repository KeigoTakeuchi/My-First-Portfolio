package com.example.demo.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class LoginUser extends User {

	private Integer id;
	
	private String displayName;
	
	public LoginUser(String username, String password,
			Collection<? extends GrantedAuthority> authorities, Integer id, String displayName) {
		super(username, password, authorities);
		// TODO 自動生成されたコンストラクター・スタブ
		this.id = id;
		this.displayName =  displayName;
	}

	public Integer getId() {
		return id;
	}
	
	public String getDisplayName() {
		return displayName;
	}
}
