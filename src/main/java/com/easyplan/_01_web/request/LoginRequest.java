package com.easyplan._01_web.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
	private String email;
	
	private String password;
}
