package com.mysite.sbb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

@EnableMethodSecurity
	//Spring Security 에서 controller에 할당된 @PreAuthorize("isAuthenticated()") 를 작동
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
			
			//로그인을 처리하도록 설정 : controller에서 처리하지 않고 Spring Security에서 처리하도록 설정 
			// "/user/login" 의 post 요청을 Security에서 처리하겠다.
			// 인증성공시 "/"		http://localhost:9696/	
			
			//로그인 처리 (Post요청 : /user/login)
			.formLogin((formLogin) -> formLogin
					.loginPage("/user/login")
					
					// Spring Security의 기본설정 : ID의 name = "username"
					//							password 필드의 name = "password" (만약에 "password"에 "pass"같이 저장이되면 제대로 작동이 되지 않으므로 밑의 설정을 한다)
					//.usernameParameter("email")
					//.passwordParameter("password") 지금은 굳이 사용하지 않음
					
					
						//UserSecurityService.java 에서 인증을 처리 후 성공시
					.defaultSuccessUrl("/")
					)
			
			
			//로그아웃 처리 (/user/logout), 세션에 적용된 모든 값을 제거
			//로그아웃 완료 시 "/"로 이동됨
			.logout((logout) -> logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
					.logoutSuccessUrl("/")
					.invalidateHttpSession(true)
					)
			
			;
		return http.build(); 
	}
	
	@Bean			//Spring 컨테이너 (IoC컨테이너-- 제어의 역전-- 객체를 개발자가 아니고 프레임워크에서 만든다는것)에 객체를 등록, 만들때 interface로 만들어야 import가 자동으로 됨.
	PasswordEncoder passwordEncoder() {		
		
		return new BCryptPasswordEncoder();
	}
	// PasswordEncoder (인터페이스) ===> BCryptPasswordEncoder(구현한 클래스)
	
	
	
	//인증을 처리하는 객체가 Bean(객체) 등록이 되어있어야함 : IoC 컨테이너에 객체를 등록
		//UserSecurityService.java가 작동되기 위한 Bean등록
	@Bean
	AuthenticationManager authenticationManager
	(AuthenticationConfiguration authenticationConfiguration) throws Exception{
		
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	
	
	
	
	
	
	
}
