package com.mysite.sbb.question;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;		//임포트 주의
import org.springframework.stereotype.Service;

import com.mysite.sbb.DataNotFoundException;
import com.mysite.sbb.user.SiteUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class QuestionService {
	
	private final QuestionRepository questionRepository; 
	
	//Question 테이블의 모든 레코드를 읽어와서 List<Question> 으로 값을 리턴하는 메소드 
	// 페이징 처리되지 않는 모든 레코드를 리턴 (사용중지-이제 이거 안쓰고 밑에 page로 씀) 
	public List<Question> getList() {
			
		List<Question> questionList = questionRepository.findAll(); 
		//return questionRepository.findAll(); 
		return questionList; 
	}
	
	// 페이징 처리해서 리턴으로 돌려줌 <사용> 
	public Page<Question> getList(int page, String kw){
		
		// Pageable 객체에 특정컬럼을 정렬할 객체를 생성해서 인자로 넣어줌
		// Sort Import 시 주의 : org.springframework.data.domain.Sort;
		List<Sort.Order> sorts = new ArrayList();
		sorts.add(Sort.Order.desc("createDate"));
			
		//page : 클라이언트에서 파라메터로 요청한 페이지 번호
		// 10  : 한 페이지에서 출력 할 레코드 갯수 (숫자를 바꾸면 보이는 개수가 바뀜)
		// createDate 컬럼을 Desc함.
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts)); 
		
		//Page<Question> pageQuestion = questionRepository.findAll(pageable);
		Page<Question> pageQuestion = questionRepository.findAllByKeyword(kw, pageable); 
		
		return pageQuestion; 
	}
	
	
	//글 상페 페이지 
	public Question getQuestion(Integer id) {
		// findById(?)
		// select * from question where id = ?; 
		
		Optional<Question> question = questionRepository.findById(id); 
		
		if (question.isPresent()) {
			//optional 내부의 객체가 null 이 아닐때  
			//Question 객체를 리턴으로 돌려줌 
			return question.get(); 
			
		}else {
			//optional 내부의 객체가 null 일때 : 예외를 강제로 발생 시킴 : DataNotFoundException 
			//throw : 예외를 강제로 발생 시킴 
			//throws : 메소드에서 메소드를 호출 하는 곳에서 예외를 처리하도로 예외를 전가 
			throw new DataNotFoundException("question not found"); 
			
		}
		
	}
	
	// 질문 제목 + 질문 내용을 DB에 저장 : insert , update, delete  <=== void
	// 글 등록
	public void create(String subject, String content, SiteUser siteUser) {
		Question question = new Question(); 
		
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		question.setAuthor(siteUser);
		
		questionRepository.save(question); 
		
	}
	
	//글 수정
	public void modify (Question question, String subject, String content) {
		//수정할 question/수정할subject/수정할content
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		
		questionRepository.save(question);
		
	}
	
	
	//글 삭제 : delete() : delete할 question객체를 가져와서 인풋
	public void delete(Question question) {
		
		
		questionRepository.delete(question);
	}
	
	
	//글 추천 등록 메소드
	public void vote(Question question, SiteUser siteuser) {
		
		//question.getVoter() ===> Set자료형임
		question.getVoter().add(siteuser);
		
		//question.getVoter()가 Set이고 add를 통해서 넣어야함
		//question.setVoter(siteuser); 오류남.
		
		questionRepository.save(question);
		
	}
	

}