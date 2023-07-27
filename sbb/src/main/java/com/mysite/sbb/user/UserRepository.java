package com.mysite.sbb.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository			//interface는 객체가 안만들어지는데, 이걸 붙여놓으면 bean에 객체처럼 만들어져서 쓸수있음. 
public interface UserRepository extends JpaRepository<SiteUser, Long> {
		//JpaRepository에서 메소드가 상속
		// 1. findAll()				: 리턴되는 것 : List<SiteUser>
		// 2. findById(정수)			: 리턴되는 것 : SiteUser
		// 3. save()	//insert, update : 리턴되는 것 : void
		// 4. delete()	: 리턴되는 것 : void
	
	
	//로그인 할 사용자 정보를 DB에서 읽어와야한다.
		//select * from SITE_USER where username = ?
	
	//select가되면 setter로 주입됨(siteuser에)
	Optional<SiteUser> findByUsername(String username);
	
}
