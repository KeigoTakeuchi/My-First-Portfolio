package com.example.demo.service.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.AccountRegisterFormDTO;
import com.example.demo.dto.LoginFormDTO;
import com.example.demo.helper.DTOConverter;
import com.example.demo.repository.AccountMapper;
import com.example.demo.service.jwt.JwtService;
import com.example.demo.service.jwt.JwtService.JwtToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	
	private final AccountMapper mapper;
	private final JwtService service;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	
	
	public record AuthenticationRequest(String name, String password) {
	}
	
	public record RegisterRequest(String name,String password,String displayName) {
	}
	
	public record AuthenticationResponse(String accessToken,String refreshToken) {	
	}
	
	
	//Account登録とJWT発行処理
	@Transactional
	public AuthenticationResponse register(AccountRegisterFormDTO accountForm) {
		
		var account = DTOConverter.convertToAccountByRegisterDTO(accountForm);
		
		account.setHashedPassword(passwordEncoder.encode(accountForm.getInputPassword()));
		
		mapper.insertAccount(account);
		
		JwtToken jwtToken = service.generateToken(account);
		
		return new AuthenticationResponse(jwtToken.token(), jwtToken.refreshToken());
	}
	
	//ログイン処理とJWT検証処理
	@Transactional(readOnly = true)
	public AuthenticationResponse authenticate(LoginFormDTO formDTO) {
		
		/*ここでLoginフォームから入力された情報をSpring Security内で認証をしている
		 * JWTはステートレスに認証情報を保持するためのトークンでしかないので、JWTを認証に
		 * 利用したりはしなくて、あくまでSpringSecurityのオーソドックスな認証後に
		 * 初めて活躍するものという認識
		 */
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						formDTO.getInputName(),
						formDTO.getInputPassword()
						));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		JwtToken jwtToken = service.generateToken(authentication);
		
		return new AuthenticationResponse(jwtToken.token(),jwtToken.refreshToken());
	}
}
