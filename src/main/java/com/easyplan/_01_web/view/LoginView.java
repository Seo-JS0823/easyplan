package com.easyplan._01_web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginView {
	
	@GetMapping("/")
	public String loginView() {
		return "login";
	}
}
