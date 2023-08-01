package com.mysite.sbb;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component		//Bean에 객체 등록 : commonUtil (객체명) ==> 이렇게 만들어짐(Bean등록됨)
public class CommonUtil {

	//@Component, @Controller, @Service, @Repository : Bean에 객체등록

	public String markdown (String markdown) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(markdown);
		HtmlRenderer render = HtmlRenderer.builder().build();
	
		return render.render(document);
	}

}
