package com.example.test_project_gradle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Controller_My {
	
	//http://localhost:8585/spring
	@GetMapping ("/spring")
	@ResponseBody			//문자열 자체를 client로 던짐 : JSON으로 전송
	public String clientRequest() {
		
		return "Welcome to My Test Spring Boot";
	}

	
	//src/main/resources/templates ==> view page : Thymeleaf를 저장.
	//http://localhost:8585/
	@GetMapping("/")
	public String index() {
		
		return "index";
	}
	
	
}
