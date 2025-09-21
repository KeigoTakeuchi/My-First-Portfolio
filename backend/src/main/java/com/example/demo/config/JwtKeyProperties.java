package com.example.demo.config;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtKeyProperties {
	
	private Resource keystoreLocation;
	
	private String keystorePassword;
	
	private String keyAlias;
	
	private String keyPassword;
	
	private RSAPublicKey publicKey;
	
	private RSAPrivateKey privateKey;
	
	@PostConstruct
	public void init() throws Exception{
		KeyStore keyStore = KeyStore.getInstance("PKCS12");
		try(InputStream is = keystoreLocation.getInputStream()){
			keyStore.load(is,keystorePassword.toCharArray());
		}
		
		privateKey = (RSAPrivateKey)keyStore.getKey(keyAlias, keyPassword.toCharArray());
		
		publicKey = (RSAPublicKey)keyStore.getCertificate(keyAlias).getPublicKey();
	}
}
