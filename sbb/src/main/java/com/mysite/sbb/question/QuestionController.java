package com.mysite.sbb.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mysite.sbb.answer.AnswerForm;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/question")
@RequiredArgsConstructor    //생성자를 이용한 객체 주입 방식 : class내부의 final 이 붙은 변수에 객체를 주입 
@Controller
public class QuestionController {
	// Controller 는 Repository를 직접 접근하지 않는다. 
		// -- 중복된 코드, 보안 
	// Controller 는 Service를 접근 한다. 
	
	
	//private final QuestionRepository questionRepository;
	private final QuestionService questionService; 
	
	//client의 /question/list 요청을 처리하는 메소드 : http://localhost:9696/question/list
	// 리스트 
	
	// http://localhost:9696/question/list?page=1
	
	@GetMapping ("/list")			//  /question/list
	public String list(Model model , @RequestParam(value="page", defaultValue="0") int page ) {
		//1. client 요청을 받는다. http://localhost:9696/question/list
		
		//2. 비즈 니스 로직 처리 
		Page<Question> paging = questionService.getList(page) ; 
		
		
		/*
		System.out.println("페이지 존재 여부 : " + paging.isEmpty());
		System.out.println("전체 게시물수(레코드수) : " + paging.getTotalElements());
		System.out.println("전체 페이지수  : " + paging.getTotalPages());
		System.out.println("페이지당 출력할 레코드 갯수 : " + paging.getSize());
		System.out.println("현재 페이지 : " + paging.getNumber());
		System.out.println("다음 페이지 여부 : " + paging.hasNext());
		System.out.println("이전 페이지 여부 : " + paging.hasPrevious());
		*/ 
		
		
		//3. 받아온 List를 client 로 전송 ( Model 객체에 저장해서 Cient로 전송 )  
		model.addAttribute("paging", paging); 
		
		return "question_list"; 
	}
	//상세 내용
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Integer id, Model model, AnswerForm answerForm) {
		
		//System.out.println("========id 변수에 들어오는 값 출력 ==========");
		//System.out.println(id);
		
		//1. 클라이언트 요청을 받음 : http://localhost:9696/question/detail/{id}
		//2. Service 에게 로직을 처리 
		Question question = questionService.getQuestion(id);
		/*
		System.out.println(" === Question 객체 출력 =====");
		System.out.println(question.getId());
		System.out.println(question.getSubject());
		System.out.println(question.getContent());
		*/ 
		
		//3. 모델 객체에 백엔드의 값을 담아서 뷰 페이지로 전송 
		model.addAttribute("question",question ); 
	
		return "question_detail"; 
		
	}
	
	//질문 등록 요청 (get 요청 ) 
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		
		return "question_form"; 
	}
	
	//폼에서 제목과 내용을 받아서 DB에 등록 로직 
	@PostMapping("/create")
	//public String questionCreate(@RequestParam String subject, @RequestParam String content) {
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		
		// 제목과 내용을 받어서 DB에 저장 
		System.out.println("제목(dto) : " + questionForm.getSubject());
		System.out.println("내용(dto) : " + questionForm.getContent());
		
		// 유효성 검사후 DB 저장함. 
		if ( bindingResult.hasErrors()) {
			return "question_form"; 
		}
		
	
		//DB에 저장 
		questionService.create(questionForm.getSubject(), questionForm.getContent());
		
		return "redirect:/question/list"; 
	}
	
	

}