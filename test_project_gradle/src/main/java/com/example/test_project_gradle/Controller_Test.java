package com.example.test_project_gradle;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class Controller_Test {
	
	//http://localhost:8585/welcome
	@GetMapping ("/welcome")
	//@ResponseBody
	public String Test() {
		
		return "test"; 
	}
	
	
	//http://localhost:8585/myspring
	@GetMapping ("/myspring")
	@ResponseBody
	public String Test2() {
		return "Welcome to My Spring Boot";
	}
}
