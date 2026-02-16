package com.easyplan._01_web.request;

import com.easyplan.domain.user.model.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {
	private String email;
	
	private String password;
	
	private String nickname;
	
	private Gender gender;
}
