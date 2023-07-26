package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	//Spring Security 를 제어하는 설정 클래스
	
	/*
	 	@Configuration		: 스프링의 각종 설정을 잡는 어노테이션
	 						  이걸 사용하면 SbbApplication클래스가 자동으로 만들어짐.
	 						  SbbApplication.java ===> 스프링 부트가 부팅될때 모든 설정을 적용하는 파일
		
		@EnableWebSecurity	: 스프링의 보안설정을 담당
		
		@Bean : 메소드 위에서 리턴으로 객체를 받고 그 객체를 Spring IoC Container에 등록
				클래스위에 
					@Component, @Controller, @Service, @Repository
	 */
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
			// http://localhost:9696/** 모든 요청은 인증없이 접근할수 있도록 허용
			.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
					.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
						//http.하나 끝

			//다른 http.시작
			// http://localhost:9696/h2-console 요청의 접근을 허용
			//.csrf().disable()		//모든 csrf를 사용하지 않도록 설정
			//h2 DB는 csrf 적용이 안되어서 다른 설정을 해줘야함. csrf없이 통신할수 있도록 허용
			.csrf((csrf)-> csrf
				.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))		//이렇게하면 접근은 허용이 된거고 그 이후에 추가 설정을 해야됨.
				)
			// H2 콘솔에서 프레임을 작동 시키도록 설정
			.headers((headers)-> headers
					.addHeaderWriter(new XFrameOptionsHeaderWriter(
							XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
							))
					)
			
			
			;
		return http.build(); 
	}
	
	@Bean			//Spring 컨테이너 (IoC컨테이너-- 제어의 역전-- 객체를 개발자가 아니고 프레임워크에서 만든다는것)에 객체를 등록, 만들때 interface로 만들어야 import가 자동으로 됨.
	PasswordEncoder passwordEncoder() {		
		
		return new BCryptPasswordEncoder();
	}
	// PasswordEncoder (인터페이스) ===> BCryptPasswordEncoder(구현한 클래스)
	

}