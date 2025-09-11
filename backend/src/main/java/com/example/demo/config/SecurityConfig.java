package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtKeyProperties jwtKeyProperties;
	
	private final UserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			.cors(cors -> cors
			.configurationSource(this.corsConfigurationSource()))
			
			.csrf(csrf -> csrf
			.disable())

			.authorizeHttpRequests(authz -> authz
			.requestMatchers("/api/authenticate/","/api/register/").permitAll()
			.requestMatchers(HttpMethod.GET,"/api/messages/","/api/users/").authenticated()
			.requestMatchers("/api/messages/","/api/users/").hasAnyRole("USER","ADMIN")
			.requestMatchers("/api/**").hasRole("ADMIN")
			.anyRequest().denyAll())
			
			.formLogin(form -> form
			.disable())
			
			.logout(logout -> logout
			.disable())
			
			.sessionManagement(session -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			
			.oauth2ResourceServer(oauth2 -> oauth2
                    .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));
			
		return http.build();
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider(PasswordEncoder passwordencoder) {
		
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordencoder);
		return provider;
	}
	
	@Bean
	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.addAllowedMethod(CorsConfiguration.ALL);
		corsConfig.addAllowedHeader(CorsConfiguration.ALL);
		//Headerのカスタム認証tokenを名前付きで認識させ、front側でJavaScriptに渡せるようにする
		corsConfig.addExposedHeader("X-AUTH-TOKEN");
		//以下の引数にCORSを通過できるフロントエンド側のURLパスの指定をする
		//corsConfig.addAllowedOrigin("ココにURL");
		corsConfig.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		corsSource.registerCorsConfiguration("/**",corsConfig);
		return corsSource;
	}
	
	//JWTの認証情報をSpringSecurityのAuthenticationオブジェクトへ変換する
	@Bean
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		
		//ここでの名前は後に生成するJWTでのclaims名と一致させる
		grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
		//@PreAuthorizeでhasRoleを利用できるようになる
		grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
		return jwtAuthenticationConverter;
	}
	
	//JSON Web Token検証用部品
	@Bean 
	public JwtDecoder jwtDecoder() {
		//公開鍵を取得し、それを検証用部品として返す
		return NimbusJwtDecoder.withPublicKey(jwtKeyProperties.getPublicKey()).build();
	}
	
	//JSON Web Token生成用部品
	@Bean 
	public JwtEncoder jwtEncoder() {
		//公開鍵と秘密鍵のペアを準備
		JWK jwk = new RSAKey.Builder(jwtKeyProperties.getPublicKey())
					.privateKey(jwtKeyProperties.getPrivateKey())
					.build();
		//鍵のペアを不変的な供給源として利用できるように設定
		JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(new JWKSet(jwk));
		//Token生成用の署名を行う部品を返す
		return new NimbusJwtEncoder(jwkSource);
	}
}
