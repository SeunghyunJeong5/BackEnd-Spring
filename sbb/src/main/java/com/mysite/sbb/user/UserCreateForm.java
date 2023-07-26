package com.mysite.sbb.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserCreateForm {
	//client 에서 넘어오는 모든 필드를 한꺼번에 주입가능(requestpram같이 일일히 하나하나 안해도됨)
	//필드의 값에 대한 유효성 체크까지 같이함. 
	
	@NotEmpty(message="사용자 ID는 필수입력 사항입니다.")		//jakarta에 있는걸 import해야함. 다른거는 엔티티에서 씀
	@Size(min=3, max=25)
	private String username;
	
	@NotEmpty(message="패스워드는 필수입력 사항입니다.")
	private String password1;
	
	@NotEmpty(message="패스워드 확인은 필수입력 사항입니다.")
	private String password2;
	
	@NotEmpty(message="메일주소는 필수입력 사항입니다.")
	@Email		// aaa@aaa.com 형식이 맞는지 확인
	private String email;
	
	
}
