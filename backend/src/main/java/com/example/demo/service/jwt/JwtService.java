package com.example.demo.service.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import com.example.demo.config.JwtConfig;
import com.example.demo.entity.Account;
import com.example.demo.entity.LoginUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtService {

	private final JwtEncoder jwtEncoder ;
	private final JwtConfig jwtConfig ;
	
	public record JwtToken(String token, String refreshToken, Date expiresAt) {
		
	}
	//Authentication(認証情報)からTokenを生成
	public JwtToken generateToken(Authentication authentication) {
		
		List<String> stringAuthority = new ArrayList<>();
		
		for (GrantedAuthority authority : authentication.getAuthorities()) {
			stringAuthority.add(authority.getAuthority());
		}
		return generateToken(authentication.getName(),stringAuthority);
	}
	
	//userDetails(認証情報)からTokenを生成
	public JwtToken generateToken(UserDetails userDetails) {
		
		List<String> stringAuthority = new ArrayList<>();
		
		for (GrantedAuthority authority : userDetails.getAuthorities()) {
			stringAuthority.add(authority.getAuthority());
		}
		
		return generateToken(userDetails.getUsername(),stringAuthority);
	}
	
	//AccountエンティティからTokenを生成
	public JwtToken generateToken(Account account) {
		
		LoginUser user = new LoginUser(
							account.getName(),
							account.getHashedPassword(),
							account.getAuthority().getAuthorities(),
							account.getId(),
							account.getDisplayName());
		return generateToken(user);
	}
	
	public JwtToken generateToken(String username,Iterable<String> roles) {
		
		//現在の時刻を取得
		Instant now = Instant.now();
		
		//現在の時刻にアクセストークンの有効期限を指定した単位で加算したもの
		Instant accessTokenExpiry = now.plus(jwtConfig.getExpiration(),ChronoUnit.MILLIS);
		
		//上記のリフレッシュトークンバージョン
		Instant refreshTokenExpiry = now.plus(jwtConfig.getRefreshExpiration(),ChronoUnit.MILLIS);
		
		//権限をクレームとしてラッピング(アクセストークンに含めたい権限)
		Map<String,Object> claims = new HashMap<>();
		
		claims.put("roles", roles);
		
		
		//アクセストークンには権限リストを格納する
		String accessToken = createToken(username, now, accessTokenExpiry, claims);
		
		//リフレッシュトークンには権限リストを格納しない
		String refreshToken = createToken(username, now, refreshTokenExpiry, new HashMap<>());
		
		return new JwtToken(accessToken, refreshToken, Date.from(accessTokenExpiry));
		
	}
	
	private String createToken(String name, Instant now, Instant expiresAt, Map<String, Object> claims) {
		
		JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
				.subject(name)
				.issuedAt(now)
				.expiresAt(expiresAt);
				
		claims.forEach(claimsBuilder :: claim);
		
		/*ここのjwtEncoderはSecurityConfigで@Bean化したインスタンスを利用している(内部の処理を読むとより分かりやすい)
		 * また、以下で今まで取得し設定してきたプロパティをJWT形式に変換している
		 */
		return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
	}
}
