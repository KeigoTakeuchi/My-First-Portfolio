package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.jwt.JwtService;
import com.example.demo.service.jwt.JwtService.JwtToken;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TokenRefreshController {

	private final JwtService jwtService;
	
	private final JwtDecoder jwtDecoder;
	
	private final UserDetailsService userDetailsService;
	
	public record TokenRefreshResponse(String accessToken, String refreshToken) {
	}
	
	public record TokenRefreshRequest(String refreshToken) {
	}
	
	
	@PostMapping("/refresh-token")
	public ResponseEntity<TokenRefreshResponse> refreshToken(
			TokenRefreshRequest refreshRequest){
		
		String tokenWithPrefix = refreshRequest.refreshToken();
		
		if (tokenWithPrefix == null || !tokenWithPrefix.startsWith("Bearer ")) {
			return ResponseEntity.badRequest().build();
		}
		try {
			String refreshToken = tokenWithPrefix.substring(7);
			
			//accessTokenの検証
			var jwt = jwtDecoder.decode(refreshToken);
			
			//JwtService内createTokenメソッドで設定したclaimsBulderのbuilderと一致させる必要がある
			String username = jwt.getSubject();
			
			//ユーザー情報の取得
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			
			JwtToken newJwtToken = jwtService.generateToken(userDetails);
			
			return ResponseEntity.ok(new TokenRefreshResponse(newJwtToken.token(),newJwtToken.refreshToken()));
			
		}catch (JwtException | BadCredentialsException e) {
			return ResponseEntity.status(401).build();
		}
	}
}
