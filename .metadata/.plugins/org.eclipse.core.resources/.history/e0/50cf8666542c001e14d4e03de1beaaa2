package com.mysite.sbb.answer;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mysite.sbb.question.Question;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	//Service  ====> Repository 
	
	private final AnswerRepository answerRepository; 
	
	//답변글 등록 : Question 객체, content 
	public void create (Question question, String content) {
		Answer answer = new Answer(); 
		
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		
		answerRepository.save(answer); 
			
	}

}
