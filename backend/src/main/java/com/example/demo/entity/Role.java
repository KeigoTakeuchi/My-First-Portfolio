package com.example.demo.entity;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
	ADMIN,USER,UNKNOWN;
	
	public List<GrantedAuthority> getAuthorities(){
		
		
		return switch (this) {
			case ADMIN -> List.of(
					new SimpleGrantedAuthority("ROLE_ADMIN"),
					new SimpleGrantedAuthority("ROLE_USER"),
					new SimpleGrantedAuthority("ROLE_UNKNOWN")
			);
			case USER -> List.of(
					new SimpleGrantedAuthority("ROLE_USER"),
					new SimpleGrantedAuthority("ROLE_UNKNOWN")
			);
			case UNKNOWN -> List.of(
					new SimpleGrantedAuthority("ROLE_UNKNOWN")
			);
		};
	}
	
	
}
