package com.example.demo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

	//Tokenの有効期限
	private long expiration;
	
	//RefreshTokenの有効期限
	private long refreshExpiration;
}
