package com.mysite.sbb.question;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class QuestionServiceTest3 {

	@Autowired
	QuestionService questionService;
	
	@Test
	void testGetListIntString() {
		
		Page<Question> paging = 
		questionService.getList(1, "스프링");
		
		System.out.println("검색된 개수 : "+paging.getSize());
		
		
	}

}