package com.example.test_project_gradle.test;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor			//기본생성자 만들어주는거
@ToString
public class BoardDTO {
	
	private String b_id;
	private String title;
	private String content;
}
